package stamboom.domain;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import stamboom.util.StringUtilities;

public class Persoon implements Serializable
{

    // ********datavelden**************************************
    private final int nr;
    private final String[] voornamen;
    private final String tussenvoegsel;
    private final Calendar gebDat;
    private final String gebPlaats;
    private Gezin ouderlijkGezin;
    private final List<Gezin> alsOuderBetrokkenIn;
    private transient ObservableList<Gezin> oGezinnen;
    private final Geslacht geslacht;
    
    private final SimpleStringProperty roepnaam;
    private final SimpleStringProperty achternaam;
    private final SimpleStringProperty gebString;

    // ********constructoren***********************************
    /**
     * er wordt een persoon gecreeerd met persoonsnummer persNr en met als
     * voornamen vnamen, achternaam anaam, tussenvoegsel tvoegsel, geboortedatum
     * gebdat, gebplaats, geslacht g en een gegeven ouderlijk gezin (mag null
     * (=onbekend) zijn); NB. de eerste letter van een voornaam, achternaam en
     * gebplaats wordt naar een hoofdletter omgezet, alle andere letters zijn
     * kleine letters; het tussenvoegsel is zo nodig in zijn geheel
     * geconverteerd naar kleine letters.
     *
     */
    Persoon(int persNr, String[] vnamen, String anaam, String tvoegsel,
            Calendar gebdat, String gebplaats, Geslacht g, Gezin ouderlijkgezin)
    {
        this.nr = persNr;
        this.voornamen = vnamen;
        this.roepnaam = new SimpleStringProperty(this.voornamen[0]);
        this.achternaam = new SimpleStringProperty(anaam);
        this.tussenvoegsel = tvoegsel;
        this.gebDat = gebdat;
        this.gebString = new SimpleStringProperty(StringUtilities.datumString(gebdat));
        this.gebPlaats = gebplaats;
        this.geslacht = g;
        this.ouderlijkGezin = ouderlijkgezin;
        this.alsOuderBetrokkenIn = new ArrayList<>();
        this.oGezinnen = FXCollections.observableList(alsOuderBetrokkenIn);
    }

    // ********methoden****************************************
    /**
     * @return de achternaam van deze persoon
     */
    public String getAchternaam()
    {
        return this.achternaam.get();
    }
    
    public String getRoepnaam()
    {
        return this.roepnaam.get();
    }
    
    public String getGebString()
    {
        return this.gebString.get();
    }

    /**
     * @return de geboortedatum van deze persoon
     */
    public Calendar getGebDat()
    {
        return this.gebDat;
    }

    /**
     *
     * @return de geboorteplaats van deze persoon
     */
    public String getGebPlaats()
    {
        return this.gebPlaats;
    }

    /**
     *
     * @return het geslacht van deze persoon
     */
    public Geslacht getGeslacht()
    {
        return this.geslacht;
    }

    /**
     *
     * @return de voorletters van de voornamen; elke voorletter wordt gevolgd
     * door een punt
     */
    public String getInitialen()
    {
        return convertInitialen(this.voornamen);
    }
    
    public String convertInitialen(String[] vnamen)
    {
        String initialen = "";
        
        for (String s : vnamen)
        {
            initialen += s.substring(0,1).toUpperCase() + ".";
        }
        
        return initialen;
    }

    /**
     *
     * @return de initialen gevolgd door een eventueel tussenvoegsel en
     * afgesloten door de achternaam; initialen, voorzetsel en achternaam zijn
     * gescheiden door een spatie
     */
    public String getNaam()
    {
        return convertNaam(getInitialen(), this.tussenvoegsel, this.achternaam.get());
    }
    
    public String convertNaam(String initialen, String tvoegsel, String anaam)
    {
        String volledigeNaam = initialen;
        
        if (tvoegsel != null && !tvoegsel.equals(""))
        {
            volledigeNaam += " " + tvoegsel;
        }
        
        volledigeNaam += " " + anaam;
        return volledigeNaam;
    }

    /**
     * @return het nummer van deze persoon
     */
    public int getNr()
    {
        return this.nr;
    }

    /**
     * @return het ouderlijk gezin van deze persoon, indien bekend, anders null
     */
    public Gezin getOuderlijkGezin()
    {
        return this.ouderlijkGezin;
    }

    /**
     * @return het tussenvoegsel van de naam van deze persoon (kan een lege
     * string zijn)
     */
    public String getTussenvoegsel()
    {
        return this.tussenvoegsel;
    }

    /**
     * @return alle voornamen onderling gescheiden door een spatie
     */
    public String getVoornamen()
    {
        StringBuilder init = new StringBuilder();
        
        for (String s : this.voornamen)
        {
            init.append(s).append(' ');
        }
            
        return init.toString().trim();
    }

    /**
     * @return de standaardgegevens van deze mens: naam (geslacht) geboortedatum
     */
    public String standaardgegevens()
    {
        return getNaam() + " (" + getGeslacht() + ") " + StringUtilities.datumString(gebDat);
    }

    @Override
    public String toString()
    {
        return standaardgegevens();
    }

    /**
     * @return de gezinnen waar deze persoon bij betrokken is
     */    
    public ObservableList<Gezin> getAlsOuderBetrokkenIn()
    {
        return (ObservableList<Gezin>) FXCollections.unmodifiableObservableList(oGezinnen);
    }

    /**
     * Als het ouderlijk gezin van deze persoon nog onbekend is dan wordt deze
     * persoon een kind van ouderlijkGezin en tevens wordt deze persoon als kind
     * in dat gezin geregistreerd Als de ouders bij aanroep al bekend zijn,
     * verandert er niets
     *
     * @param ouderlijkGezin
     */
    void setOuders(Gezin ouderlijkgezin)
    {
        if (this.ouderlijkGezin == null)
        {
            this.ouderlijkGezin = ouderlijkgezin;
            this.ouderlijkGezin.breidUitMet(this);
        }
    }

    /**
     * @return voornamen, eventueel tussenvoegsel en achternaam, geslacht,
     * geboortedatum, namen van de ouders, mits bekend, en nummers van de
     * gezinnen waarbij deze persoon betrokken is (geweest)
     */
    public String beschrijving()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(standaardgegevens());

        if (this.ouderlijkGezin != null)
        {
            sb.append("; 1e ouder: ").append(this.ouderlijkGezin.getOuder1().getNaam());
            
            if (this.ouderlijkGezin.getOuder2() != null)
            {
                sb.append("; 2e ouder: ").append(this.ouderlijkGezin.getOuder2().getNaam());
            }
        }
        
        if (!this.alsOuderBetrokkenIn.isEmpty())
        {
            sb.append("; is ouder in gezin ");

            for (Gezin g : this.alsOuderBetrokkenIn)
            {
                sb.append(g.getNr()).append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * als g nog niet bij deze persoon staat geregistreerd wordt g bij deze
     * persoon geregistreerd en anders verandert er niets
     *
     * @param g een nieuw gezin waarin deze persoon een ouder is
     *
     */
    void wordtOuderIn(Gezin g)
    {
        if (!this.oGezinnen.contains(g))
        {
            this.oGezinnen.add(g);
        }
    }

    /**
     *
     *
     * @param andereOuder mag null zijn
     * @return het ongehuwde gezin met de andere ouder ; mits bestaand anders
     * null
     */
    public Gezin heeftOngehuwdGezinMet(Persoon andereOuder)
    {
        Gezin gezin = null;
        
        for (Gezin g : this.alsOuderBetrokkenIn)
        {
            if (g.isOngehuwd() == true)
            {
                if (andereOuder != null)
                {
                    if (g.getOuder1() == andereOuder || g.getOuder2() == andereOuder)
                    {
                        //Ongehuwd gezin
                        gezin = g;
                    }
                }
                else
                {
                    //Eenouder gezin
                    gezin = g;
                }
            }
        }
        
        return gezin;
    }

    /**
     *
     * @param datum
     * @return true als persoon op datum getrouwd is, anders false
     */
    public boolean isGetrouwdOp(Calendar datum)
    {
        for (Gezin gezin : this.alsOuderBetrokkenIn)
        {
            if (gezin.isHuwelijkOp(datum))
            {
                return true;
            }
        }
        
        return false;
    }

    /**
     *
     * @param datum
     * @return true als de persoon kan trouwen op datum, hierbij wordt rekening
     * gehouden met huwelijken in het verleden en in de toekomst
     */
    public boolean kanTrouwenOp(Calendar datum)
    {
        for (Gezin gezin : this.alsOuderBetrokkenIn)
        {
            if (gezin.heeftGetrouwdeOudersOp(datum))
            {
                return false;
            } 
            else
            {
                Calendar huwdatum = gezin.getHuwelijksdatum();
                
                if (huwdatum != null && huwdatum.after(datum))
                {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     *
     * @param datum
     * @return true als persoon op datum gescheiden is, anders false
     */
    public boolean isGescheidenOp(Calendar datum)
    {
        for (Gezin g : this.alsOuderBetrokkenIn)
        {
            if (g.getScheidingsdatum().compareTo(datum) == 0)
            {
                return true;
            }
        }
            
        return false;
    }

    /**
     * ********* de rest wordt in opgave 2 verwerkt ****************
     */
    /**
     *
     * @return het aantal personen in de stamboom van deze persoon (ouders,
     * grootouders etc); de persoon zelf telt ook mee
     */
    public int afmetingStamboom()
    {
        //geen ouders
        int aantalPersonen = 1;
        if(this.getOuderlijkGezin() != null){
            if(this.getOuderlijkGezin().getOuder1()!= null){
                //aantal ouders van ouder 1 ophalen
                aantalPersonen += this.getOuderlijkGezin().getOuder1().afmetingStamboom();
            }
            
            if(this.getOuderlijkGezin().getOuder2()!= null){
                //aantal ouders van ouder 1 ophalen
                aantalPersonen += this.getOuderlijkGezin().getOuder2().afmetingStamboom();
            }
        }
        
        return aantalPersonen;
    }

    /**
     * de lijst met de items uit de stamboom van deze persoon wordt toegevoegd
     * aan lijst, dat wil zeggen dat begint met de toevoeging van de
     * standaardgegevens van deze persoon behorende bij generatie g gevolgd door
     * de items uit de lijst met de stamboom van de eerste ouder (mits bekend)
     * en gevolgd door de items uit de lijst met de stamboom van de tweede ouder
     * (mits bekend) (het generatienummer van de ouders is steeds 1 hoger)
     *
     * @param lijst
     * @param g >=0, het nummer van de generatie waaraan deze persoon is
     * toegewezen;
     */
    void voegJouwStamboomToe(ArrayList<PersoonMetGeneratie> lijst, int g)
    {
        //standaard gegevens toevoegen van persoon zelf
        lijst.add(new PersoonMetGeneratie(this.standaardgegevens(),g));
        g++;
        if(ouderlijkGezin != null)
        {
            //standaard gegevens van ouder 1 toevoegen en de ouders daarvan
            if(ouderlijkGezin.getOuder1() != null)
            {
                this.ouderlijkGezin.getOuder1().voegJouwStamboomToe(lijst,g);
            }

            //standaard gegevens van ouder 2 toevoegen en de ouders daarvan
            if(ouderlijkGezin.getOuder2() != null)
            {
                this.ouderlijkGezin.getOuder2().voegJouwStamboomToe(lijst, g);
            }
        }
    }

    /**
     *
     * @return de stamboomgegevens van deze persoon in de vorm van een String:
     * op de eerste regel de standaardgegevens van deze persoon, gevolgd door de
     * stamboomgegevens van de eerste ouder (mits bekend) en gevolgd door de
     * stamboomgegevens van de tweede ouder (mits bekend); formattering: iedere
     * persoon staat op een aparte regel en afhankelijk van het
     * generatieverschil worden er per persoon 2*generatieverschil spaties
     * ingesprongen;
     *
     * bijv voor M.G. Pieterse met ouders, grootouders en overgrootouders,
     * inspringen is in dit geval met underscore gemarkeerd: <br>
     *
     * M.G. Pieterse (VROUW) 5-5-2004<br>
     * __L. van Maestricht (MAN) 27-6-1953<br>
     * ____A.G. von Bisterfeld (VROUW) 13-4-1911<br>
     * ______I.T.M.A. Goldsmid (VROUW) 22-12-1876<br>
     * ______F.A.I. von Bisterfeld (MAN) 27-6-1874<br>
     * ____H.C. van Maestricht (MAN) 17-2-1909<br>
     * __J.A. Pieterse (MAN) 23-6-1964<br>
     * ____M.A.C. Hagel (VROUW) 12-0-1943<br>
     * ____J.A. Pieterse (MAN) 4-8-1923<br>
     */
    public String stamboomAlsString()
    {
        StringBuilder builder = new StringBuilder();
        ArrayList<PersoonMetGeneratie> lijst = new ArrayList<>();
        
        //Ophalen van de stamboom
        voegJouwStamboomToe(lijst, 0);
        
        for(PersoonMetGeneratie pg : lijst)
        {
            for(int g = 0; g < pg.getGeneratie(); g++)
            {
                builder.append("  ");
            }
            builder.append(pg.getPersoonsgegevens());
            builder.append(System.getProperty("line.separator"));
        }

        return builder.toString();
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
        oGezinnen = FXCollections.observableList(alsOuderBetrokkenIn);
    }
}
