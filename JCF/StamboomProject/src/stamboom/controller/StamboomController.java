/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import stamboom.domain.Administratie;
import stamboom.storage.DatabaseMediator;
import stamboom.storage.IStorageMediator;
import stamboom.storage.SerializationMediator;

public class StamboomController
{
    private Administratie admin;
    private IStorageMediator storageMediator;

    /**
     * creatie van stamboomcontroller met lege administratie en onbekend
     * opslagmedium
     */
    public StamboomController()
    {
        admin = new Administratie();
        storageMediator = null;
    }

    public Administratie getAdministratie()
    {
        return admin;
    }

    /**
     * administratie wordt leeggemaakt (geen personen en geen gezinnen)
     */
    public void clearAdministratie()
    {
        admin = new Administratie();
    }

    /**
     * administratie wordt in geserialiseerd bestand opgeslagen
     *
     * @param bestand
     * @throws IOException
     */

    public void serialize(File bestand) throws IOException 
    {        
        Properties props = new Properties();
        props.put("file", bestand);
        this.storageMediator = new SerializationMediator();
        this.storageMediator.configure(props);
        this.storageMediator.save(this.admin); 
    }

    /**
     * administratie wordt vanuit geserialiseerd bestand gevuld
     *
     * @param bestand
     * @throws IOException
     */
    public void deserialize(File bestand) throws IOException 
    {
        Properties props = new Properties();
        props.put("file", bestand);
        this.storageMediator = new SerializationMediator();
        this.storageMediator.configure(props);
        this.admin = this.storageMediator.load();
    }
    
    // opgave 4
    private void initDatabaseMedium() throws IOException 
    {
        if (!(storageMediator instanceof DatabaseMediator))
        {
            Properties props = new Properties();            
            try (FileInputStream in = new FileInputStream("database.properties"))
            {
                props.load(in);
            }
            
            this.storageMediator = new DatabaseMediator();
            this.storageMediator.configure(props);
        }
    }
    
    /**
     * administratie wordt vanuit standaarddatabase opgehaald
     *
     * @throws IOException
     */
    public void loadFromDatabase() throws IOException
    {
        /*Properties props = new Properties();
        props.put("driver", "com.mysql.jdbc.Driver");
        props.put("url", "jdbc:mysql:localhost:3306/dbstamboom");
        props.put("username", "root");
        props.put("password", "IJsje!123");*/        
        
        initDatabaseMedium();        
        this.admin = this.storageMediator.load();
    }

    /**
     * administratie wordt in standaarddatabase bewaard
     *
     * @throws IOException
     */
    public void saveToDatabase() throws IOException
    {
        /*Properties props = new Properties();
        props.put("driver", "mysql");
        props.put("url", "localhost:3306/dbstamboom");
        props.put("username", "root");
        props.put("password", "IJsje!123");*/
        
        initDatabaseMedium();
        this.storageMediator.save(this.admin); 
    }
}
