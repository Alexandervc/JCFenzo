package stamboom.console;

public enum MenuItem
{
    EXIT("exit"),
    NEW_PERS("registreer persoon"),
    NEW_ONGEHUWD_GEZIN("registreer ongehuwd gezin"),
    NEW_HUWELIJK("registreer huwelijk"),
    SCHEIDING("registreer scheiding"),
    SHOW_PERS("toon gegevens persoon"),
    SHOW_GEZIN("toon gegevens gezin"),
    SAVE_ADMIN("save administratie"),
    LOAD_ADMIN("laad administratie"),
    SHOW_STAMBOOM("toon stamboom persoon");
    
    private final String omschr;

    private MenuItem(String omschr)
    {
        this.omschr = omschr;
    }

    /**
     * @return  the omschr
     * @uml.property  name="omschr"
     */
    public String getOmschr() 
    {
        return omschr;
    }
}
