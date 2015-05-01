package stamboom.domain;

import java.io.*;
import java.util.*;
import javafx.collections.*;

public class Administratie implements Serializable
{

    //************************datavelden*************************************
    private int nextGezinsNr;
    private int nextPersNr;
    private final List<Persoon> personen;
    private final List<Gezin> gezinnen;
    private transient ObservableList<Persoon> oPersonen;
    private transient ObservableList<Gezin> oGezinnen;

    //***********************constructoren***********************************
    /**
     * er wordt een administratie gecreeerd met 0 personen en dus 0 gezinnen
     * personen en gezinnen die in de toekomst zullen worden gecreeerd, worden
     * elk opvolgend genummerd vanaf 1
     */
    public Administratie()
    {
        this.nextGezinsNr = 1;
        this.nextPersNr = 1;
        this.personen = new ArrayList<>();
        this.gezinnen = new ArrayList<>(); 
        this.oPersonen = FXCollections.observableList(personen);
        this.oGezinnen = FXCollections.observableList(gezinnen);
    }

    //**********************methoden****************************************
    /**
     * er wordt een persoon met een gegeven geslacht, met als voornamen vnamen,
     * achternaam anaam, tussenvoegsel tvoegsel, geboortedatum gebdat,
     * geboorteplaats gebplaats en een gegeven ouderlijk gezin gecreeerd; de persoon
     * krijgt een uniek nummer toegewezen de persoon is voortaan ook bij het
     * ouderlijk gezin bekend. Voor de voornamen, achternaam en gebplaats geldt
     * dat de eerste letter naar een hoofdletter en de resterende letters naar
     * een kleine letter zijn geconverteerd; het tussenvoegsel is zo nodig in
     * zijn geheel geconverteerd naar kleine letters; overbodige spaties zijn 
     * verwijderd
     *
     * @param geslacht
     * @param vnamen vnamen.length>0; alle strings zijn niet leeg
     * @param anaam niet leeg
     * @param tvoegsel
     * @param gebdat
     * @param gebplaats niet leeg
     * @param ouderlijkGezin mag de waarde null (=onbekend) hebben
     *
     * @return als de persoon al bekend was (op basis van combinatie van getNaam(),
     * geboorteplaats en geboortedatum), wordt er null geretourneerd, anders de 
     * nieuwe persoon
     */
    public Persoon addPersoon(Geslacht geslacht, String[] vnamen, String anaam,
            String tvoegsel, Calendar gebdat,
            String gebplaats, Gezin ouderlijkGezin)
    {
        if (vnamen.length == 0)
        {
            throw new IllegalArgumentException("ten minst 1 voornaam");
        }
        
        for (String voornaam : vnamen)
        {
            if (voornaam.trim().isEmpty())
            {
                throw new IllegalArgumentException("lege voornaam is niet toegestaan");
            }
        }

        if (anaam.trim().isEmpty())
        {
            throw new IllegalArgumentException("lege achternaam is niet toegestaan");
        }

        if (gebplaats.trim().isEmpty())
        {
            throw new IllegalArgumentException("lege geboorteplaats is niet toegestaan");
        }
        
        int resultaten = 0;
        
        for (Persoon p : this.oPersonen)
        {
            String initialen = p.convertInitialen(vnamen);
            String naam = p.convertNaam(initialen, tvoegsel, anaam);
            
            if (p.getNaam().toLowerCase().equals(naam.toLowerCase())
                    && p.getGebPlaats().toLowerCase().equals(gebplaats.toLowerCase())
                    && p.getGebDat().compareTo(gebdat) == 0)
            {
                resultaten++;
            }
        }
        
        if (resultaten >= 1)
        {
            return null;
        }
        else
        {
            List<String> newVnamen = new ArrayList<>();
            
            for (String vnaam : vnamen)
            {
                vnaam = vnaam.replace(" ", "");
                newVnamen.add(firstCharToUppercase(vnaam));
            }
            
            String[] newVnamen2 = newVnamen.toArray(new String[newVnamen.size()]);
            
            Persoon persoon = new Persoon(this.nextPersNr, newVnamen2, firstCharToUppercase(anaam),
                    tvoegsel.toLowerCase(), gebdat, firstCharToUppercase(gebplaats), geslacht, ouderlijkGezin);
            this.oPersonen.add(persoon);
            
            if(ouderlijkGezin != null)
            {
                ouderlijkGezin.breidUitMet(persoon);
            }
            
            nextPersNr++;
            return persoon;
        }
    }
    
    public String firstCharToUppercase(String s)
    {
        s = s.replace(" ", "");
        s = s.toLowerCase();
        s = s.substring(0,1).toUpperCase() + s.substring(1);        
        return s;
    }

    /**
     * er wordt, zo mogelijk (zie return) een (kinderloos) ongehuwd gezin met
     * ouder1 en ouder2 als ouders gecreeerd; de huwelijks- en scheidingsdatum
     * zijn onbekend (null); het gezin krijgt een uniek nummer toegewezen; dit
     * gezin wordt ook bij de afzonderlijke ouders geregistreerd;
     *
     * @param ouder1
     * @param ouder2 mag null zijn
     *
     * @return null als ouder1 = ouder2 of als de volgende voorwaarden worden
     * overtreden: 1) een van de ouders is op dit moment getrouwd 2) het koppel
     * uit een ongehuwd gezin kan niet tegelijkertijd als koppel bij een ander
     * ongehuwd gezin betrokken zijn anders het gewenste gezin
     */
    public Gezin addOngehuwdGezin(Persoon ouder1, Persoon ouder2)
    {
        if (ouder1 == ouder2)
        {
            return null;
        }

        Calendar nu = Calendar.getInstance();
        
        if (ouder1.isGetrouwdOp(nu) || (ouder2 != null
                && ouder2.isGetrouwdOp(nu))
                || ongehuwdGezinBestaat(ouder1, ouder2))
        {
            return null;
        }

        Gezin gezin = new Gezin(this.nextGezinsNr, ouder1, ouder2);
        this.nextGezinsNr++;
        this.gezinnen.add(gezin);
        ouder1.wordtOuderIn(gezin);
        
        if (ouder2 != null)
        {
            ouder2.wordtOuderIn(gezin);
        }

        return gezin;
    }

    /**
     * Als het ouderlijk gezin van persoon nog onbekend is dan wordt persoon een
     * kind van ouderlijkGezin en tevens wordt persoon als kind in dat gezin
     * geregistreerd; <br>
     * Als de ouders bij aanroep al bekend zijn, verandert er
     * niets
     *
     * @param persoon
     * @param ouderlijkGezin
     */
    public void setOuders(Persoon persoon, Gezin ouderlijkGezin)
    {
        persoon.setOuders(ouderlijkGezin);
    }

    /**
     * als de ouders van dit gezin gehuwd zijn en nog niet gescheiden en datum
     * na de huwelijksdatum ligt, wordt dit de scheidingsdatum. Anders gebeurt
     * er niets.
     *
     * @param gezin
     * @param datum
     * @return true als scheiding geaccepteerd, anders false
     */
    public boolean setScheiding(Gezin gezin, Calendar datum) 
    {
        if (gezin.getHuwelijksdatum() != null && datum.after(gezin.getHuwelijksdatum()))
        {
            return gezin.setScheiding(datum);
        }
        
        return false;
    }

    /**
     * registreert het huwelijk, mits gezin nog geen huwelijk is en beide ouders
     * op deze datum mogen trouwen (pas op: ook de toekomst kan hierbij een rol
     * spelen omdat toekomstige gezinnen eerder zijn geregisteerd)
     *
     * @param gezin
     * @param datum de huwelijksdatum
     * @return false als huwelijk niet mocht worden voltrokken, anders true
     */
    public boolean setHuwelijk(Gezin gezin, Calendar datum) 
    {
        return gezin.setHuwelijk(datum);
    }

    /**
     *
     * @param ouder1
     * @param ouder2
     * @return true als dit koppel (ouder1,ouder2) al een ongehuwd gezin vormt
     */
    boolean ongehuwdGezinBestaat(Persoon ouder1, Persoon ouder2) 
    {
        if (ouder1 != null)
        {
            if (ouder1.heeftOngehuwdGezinMet(ouder2) != null)
            {
                return true;
            }
        }
        
        return false;
    }

    /**
     * als er al een ongehuwd gezin voor dit koppel bestaat, wordt het huwelijk
     * voltrokken, anders wordt er zo mogelijk (zie return) een (kinderloos)
     * gehuwd gezin met ouder1 en ouder2 als ouders gecreeerd; de
     * scheidingsdatum is onbekend (null); het gezin krijgt een uniek nummer
     * toegewezen; dit gezin wordt ook bij de afzonderlijke ouders
     * geregistreerd;
     *
     * @param ouder1
     * @param ouder2
     * @param huwdatum
     * @return null als ouder1 = ouder2 of als een van de ouders getrouwd is
     * anders het gehuwde gezin
     */
    public Gezin addHuwelijk(Persoon ouder1, Persoon ouder2, Calendar huwdatum) 
    {
        Gezin g = null;
        
        if (ouder1 == ouder2)
        {
            return null;
        }
        else
        {
            if (ouder1.kanTrouwenOp(huwdatum) && ouder2.kanTrouwenOp(huwdatum))
            {                
                if (ouder1.heeftOngehuwdGezinMet(ouder2) == null)
                {
                    g = new Gezin(this.nextGezinsNr, ouder1, ouder2);
                    this.gezinnen.add(g);
                    this.nextGezinsNr++;
                    ouder1.wordtOuderIn(g);
                    ouder2.wordtOuderIn(g);
                }
                else
                {
                    g = ouder1.heeftOngehuwdGezinMet(ouder2);
                }
                
                g.setHuwelijk(huwdatum);
            }
            else
            {
                return null;
            }
        }
        
        return g;
    }

    /**
     *
     * @return het aantal geregistreerde personen
     */
    public int aantalGeregistreerdePersonen() 
    {
        return nextPersNr - 1;
    }

    /**
     *
     * @return het aantal geregistreerde gezinnen
     */
    public int aantalGeregistreerdeGezinnen() 
    {
        return nextGezinsNr - 1;
    }

    /**
     *
     * @param nr
     * @return de persoon met nummer nr, als die niet bekend is wordt er null
     * geretourneerd
     */
    public Persoon getPersoon(int nr) 
    {
        for (Persoon p : this.personen)
        {
            if (p.getNr() == nr)
            {
                return p;
            }
        }

        return null;
    }

    /**
     * @param achternaam
     * @return alle personen met een achternaam gelijk aan de meegegeven
     * achternaam (ongeacht hoofd- en kleine letters)
     */
    public ArrayList<Persoon> getPersonenMetAchternaam(String achternaam) 
    {
        ArrayList<Persoon> pers = new ArrayList<>();
        
        for (Persoon p : this.personen)
        {
            if (p.getAchternaam().toLowerCase().equals(achternaam.toLowerCase()))
            {
                pers.add(p);
            }
        }

        return pers;
    }

    /**
     *
     * @return de geregistreerde personen
     */    
    public ObservableList<Persoon> getPersonen()
    {
        if (this.personen.size() > 0)
        {
            return (ObservableList<Persoon>) FXCollections.unmodifiableObservableList(oPersonen);
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param vnamen
     * @param anaam
     * @param tvoegsel
     * @param gebdat
     * @param gebplaats
     * @return de persoon met dezelfde initialen, tussenvoegsel, achternaam,
     * geboortedatum en -plaats mits bekend (ongeacht hoofd- en kleine letters),
     * anders null
     */
    public Persoon getPersoon(String[] vnamen, String anaam, String tvoegsel,
            Calendar gebdat, String gebplaats) 
    {        
        for (Persoon p : this.personen)
        {            
            String initialen = p.convertInitialen(vnamen);
            String naam = p.convertNaam(initialen, tvoegsel, anaam);
            
            if (p.getNaam().toLowerCase().equals(naam.toLowerCase())
                    && p.getGebDat().compareTo(gebdat) == 0
                    && p.getGebPlaats().toLowerCase().equals(gebplaats.toLowerCase()))
            {
                return p;
            }
        }
        
        return null;
    }

    /**
     *
     * @return de geregistreerde gezinnen
     */
    public ObservableList<Gezin> getGezinnen()
    {
        if (this.gezinnen.size() > 0)
        {
            return (ObservableList<Gezin>) FXCollections.unmodifiableObservableList(oGezinnen);
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param gezinsNr
     * @return het gezin met nummer nr. Als dat niet bekend is wordt er null
     * geretourneerd
     */
    public Gezin getGezin(int gezinsNr) 
    {
        // aanname: er worden geen gezinnen verwijderd
        if (1 <= gezinsNr && 1 <= this.gezinnen.size()) 
        {
            return this.gezinnen.get(gezinsNr - 1);
        }
        
        return null;
    }
    
    /**
     * Wordt aangeroepen tijdens inlezen van geserialiseerd object
     * @param ois ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
    {
        ois.defaultReadObject();
        oPersonen = FXCollections.observableList(personen);
    }
}
