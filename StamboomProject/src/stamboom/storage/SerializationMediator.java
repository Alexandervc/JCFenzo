/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stamboom.storage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import stamboom.domain.Administratie;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SerializationMediator implements IStorageMediator
{

    private Properties props;

    /**
     * creation of a non configured serialization mediator
     */
    public SerializationMediator()
    {
        props = null;
    }

    @Override
    public Administratie load() throws IOException
    {
        if (!isCorrectlyConfigured())
        {
            throw new RuntimeException("Serialization mediator isn't initialized correctly.");
        }
        
        FileInputStream stream = null;
        
        try
        {
            File bestand = (File)props.get("file");
            stream = new FileInputStream(bestand);
            ObjectInputStream in = new ObjectInputStream(stream);
            Administratie admin = (Administratie) in.readObject();
            return admin;
        }
        catch (ClassNotFoundException exc)
        {
            exc.printStackTrace();
            return null;
        }
        finally
        {
            stream.close();
        }
    }

    @Override
    public void save(Administratie admin) throws IOException
    {
        if (!isCorrectlyConfigured())
        {
            throw new RuntimeException("Serialization mediator isn't initialized correctly.");
        }
        
        FileOutputStream stream = null;
        
        try
        {
            File bestand = (File)props.get("file");
            stream = new FileOutputStream(bestand);
            ObjectOutputStream out = new ObjectOutputStream(stream);
            out.writeObject(admin);
        }
        finally
        {
            stream.close();
        }
    }

    @Override
    public boolean configure(Properties props)
    {
        this.props = props;
        return isCorrectlyConfigured();
    }

    @Override
    public Properties config()
    {
        return props;
    }

    /**
     *
     * @return true if config() contains at least a key "file" and the
     * corresponding value is a File-object, otherwise false
     */
    @Override
    public boolean isCorrectlyConfigured()
    {
        if (props == null)
        {
            return false;
        }
        
        if (props.containsKey("file"))
        {
            return props.get("file") instanceof File;
        }
        else
        {
            return false;
        }
    }
}
