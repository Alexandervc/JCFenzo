/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import stamboom.domain.*;
import stamboom.util.StringUtilities;

public class DatabaseMediator implements IStorageMediator {

    private Properties props;
    private Connection conn;

    
    @Override
    public Administratie load() throws IOException
    {
        Administratie admin = new Administratie();
        
        try
        {
            initConnection();            
            Statement stat = conn.createStatement();
            String query = "SELECT * FROM Personen";
            ResultSet rs = stat.executeQuery(query);
            
            while(rs.next())
            {
                String[] vnamen = rs.getString("voornamen").split(" ");
                String anaam = rs.getString("achternaam");
                String tvoegsel = rs.getString("tussenvoegsel");
                Calendar gebdat = StringUtilities.datum(rs.getString("geboortedatum"));
                String gebplaats = rs.getString("geboorteplaats");
                
                String geslachtstring = rs.getString("geslacht");
                Geslacht geslacht = null;
                
                if (geslachtstring.equals("MAN"))
                {
                    geslacht = Geslacht.MAN;
                }
                else if (geslachtstring.equals("VROUW"))
                {
                    geslacht = Geslacht.VROUW;
                }
                
                admin.addPersoon(geslacht, vnamen, anaam, tvoegsel, gebdat, gebplaats, null);
            }
            
            String query2 = "SELECT * FROM Gezinnen";
            ResultSet rs2 = stat.executeQuery(query2);
            
            while(rs2.next())
            {
                Gezin g = null;
                int ouder1 = rs2.getInt("ouder1");
                Persoon o1 = admin.getPersoon(ouder1);
                
                int ouder2 = 0;
                
                try
                {
                    ouder2 = rs2.getInt("ouder2");
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.getMessage());
                } 
                
                if (ouder2 > 0)
                {
                    Persoon o2 = admin.getPersoon(ouder2);                        
                    g = admin.addOngehuwdGezin(o1, o2);
                }
                else
                {
                    g = admin.addOngehuwdGezin(o1, null);
                }
                
                String huwstring = rs2.getString("huwelijksdatum");
                Calendar huwdat = null;                
                
                if (!huwstring.equals(""))
                {
                    try
                    {
                        huwdat = StringUtilities.datum(huwstring);                    
                    }
                    catch (IllegalArgumentException ex)
                    {
                        System.err.println(ex.getMessage());
                    }
                }
                
                if (huwdat != null)
                {
                    admin.setHuwelijk(g, huwdat);
                }
                
                String scheistring = rs2.getString("scheidingsdatum");
                Calendar scheidat = null;
                
                if (!scheistring.equals(""))
                {
                    try
                    {
                        scheidat = StringUtilities.datum(scheistring);                    
                    }
                    catch (IllegalArgumentException ex)
                    {
                        System.err.println(ex.getMessage());
                    }
                }
                
                if (scheidat != null)
                {
                    admin.setScheiding(g, scheidat);
                }
            }
            
            String query3 = "SELECT * FROM Personen";
            ResultSet rs3 = stat.executeQuery(query3);
            
            while(rs3.next())
            {
                String vnamen = rs3.getString("voornamen");
                String anaam = rs3.getString("achternaam");
                
                int gezin = 0;
                
                try
                {
                    gezin = rs3.getInt("ouders");
                }
                catch (SQLException ex)
                {
                    System.err.println(ex.getMessage());
                }                
                
                Gezin ouderlijkgezin = null;
                
                if (gezin > 0)
                {
                    ouderlijkgezin = admin.getGezin(gezin);
                    
                    for (Persoon p : admin.getPersonen())
                    {
                        if (p.getVoornamen().equals(vnamen) && p.getAchternaam().equals(anaam))
                        {
                            admin.setOuders(p, ouderlijkgezin);
                        }
                    }
                }
            }
        }
        catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            System.err.println(ex.getMessage());
        }
        finally
        {
            closeConnection();
        }
               
        return admin;
    }

    @Override
    public void save(Administratie administratie) throws IOException 
    {
        Administratie admin = null;
        
        try
        {
            initConnection();
            admin = administratie;
            Statement stat = conn.createStatement();
            String query_1 = "SET FOREIGN_KEY_CHECKS = 0;";
            stat.executeUpdate(query_1);
            String query_2 = "TRUNCATE Personen;";
            stat.executeUpdate(query_2);
            String query2_1 = "TRUNCATE Gezinnen;";
            stat.executeUpdate(query2_1);
            String query2_2 = "SET FOREIGN_KEY_CHECKS = 1;";
            stat.executeUpdate(query2_2);
            
            String query3 = "INSERT INTO Personen(persoonsNummer, achternaam, voornamen, tussenvoegsel, geboortedatum, geboorteplaats, geslacht, ouders) VALUES( ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pstat = conn.prepareStatement(query3);
            
            for (Persoon p : admin.getPersonen())
            {
                int persNr = p.getNr();
                pstat.setInt(1, persNr);
                
                String anaam = p.getAchternaam();
                pstat.setString(2, anaam);
                
                String vnamen = p.getVoornamen();
                pstat.setString(3, vnamen);
                
                String tvoegsel = p.getTussenvoegsel();
                pstat.setString(4, tvoegsel);
                
                String gebdat = StringUtilities.datumString(p.getGebDat());
                pstat.setString(5, gebdat);
                
                String gebplaats = p.getGebPlaats();
                pstat.setString(6, gebplaats);
                
                if (p.getGeslacht() != null)
                {
                    String geslacht = p.getGeslacht().name();
                    pstat.setString(7, geslacht);
                }
                else
                {
                    String geslacht = "";
                    pstat.setString(7, geslacht);
                }
                
                pstat.setNull(8, Types.INTEGER);               
                
                pstat.executeUpdate();
            }
            
            if (admin.getGezinnen() != null)
            {
                String query4 = "INSERT INTO Gezinnen(gezinsNummer, ouder1, ouder2, huwelijksdatum, scheidingsdatum) VALUES(?, ?, ?, ?, ?);";
                PreparedStatement pstat2 = conn.prepareStatement(query4);

                for (Gezin g : admin.getGezinnen())
                {
                    int gezinsNr = g.getNr();
                    pstat2.setInt(1, gezinsNr);

                    Persoon ouder1 = g.getOuder1();
                    int o1 = ouder1.getNr();
                    pstat2.setInt(2, o1);
                    
                    String huwdat = ""; 
                    if (g.getHuwelijksdatum() != null)
                    {
                        huwdat = StringUtilities.datumString(g.getHuwelijksdatum());
                    }
                    pstat2.setString(4, huwdat);

                    String scheidat = "";
                    if (g.getScheidingsdatum() != null)
                    {
                        scheidat = StringUtilities.datumString(g.getScheidingsdatum());
                    }
                    pstat2.setString(5, scheidat);

                    Persoon ouder2 = g.getOuder2();

                    if (ouder2 == null)
                    {
                        pstat2.setNull(3, Types.INTEGER);
                    }
                    else
                    {
                        int o2 = ouder2.getNr();
                        pstat2.setInt(3, o2);
                    }

                    pstat2.executeUpdate();
                }
            }
            
            for (Persoon p : admin.getPersonen())
            {
                if (p.getOuderlijkGezin() != null)
                {
                    String query5 = "UPDATE Personen SET ouders = ? WHERE persoonsnummer = ?";
                    PreparedStatement pstat3 = conn.prepareStatement(query5);
                    
                    int ouders = p.getOuderlijkGezin().getNr();
                    pstat3.setInt(1, ouders);
                    
                    int persNr = p.getNr();
                    pstat3.setInt(2, persNr);
                    
                    pstat3.executeUpdate();
                } 
            }
        }
        catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            System.err.println(ex.getMessage());
        }
        finally
        {
            closeConnection();
        }    
    }

    @Override
    public final boolean configure(Properties props)
    {
        this.props = props;

        try
        {
            initConnection();
            return isCorrectlyConfigured();
        }
        catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            System.err.println(ex.getMessage());
            this.props = null;
            return false;
        }
        finally
        {
            closeConnection();
        }
    }

    @Override
    public Properties config()
    {
        return props;
    }

    @Override
    public boolean isCorrectlyConfigured()
    {
        if (props == null)
        {
            return false;
        }        
        if (!props.containsKey("driver"))
        {
            return false;
        }        
        if (!props.containsKey("url"))
        {
            return false;
        }        
        if (!props.containsKey("username"))
        {
            return false;
        }        
        if (!props.containsKey("password"))
        {
            return false;
        }
        
        return true;
    }

    private void initConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {              
        conn = null;
        
        if (isCorrectlyConfigured())
        {
            String driver = (String)props.get("driver");
            String url = (String)props.get("url");
            String username = (String)props.get("username");
            String password = (String)props.get("password");
            
            Class.forName(driver).newInstance();
            conn = (Connection)DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database");
        }
        else
        {
            throw new RuntimeException("Database mediator isn't initialized correctly.");
        }
    }

    private void closeConnection()
    {
        try
        {
            conn.close();
            conn = null;
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
}
