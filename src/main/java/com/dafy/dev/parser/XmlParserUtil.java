package com.dafy.dev.parser;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.EntityReplacementMap;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;

/**
 * Created by chunxiaoli on 10/18/16.
 */
public class XmlParserUtil {
    //--------------------------/
    //- Class/Member Variables -/
    //--------------------------/

    /**
     * If set the parser will be loaded with all single characters
     * from the XHTML specification.
     * The entities used:
     * <ul>
     * <li>http://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent</li>
     * <li>http://www.w3.org/TR/xhtml1/DTD/xhtml-special.ent</li>
     * <li>http://www.w3.org/TR/xhtml1/DTD/xhtml-symbol.ent</li>
     * </ul>
     */
    public static boolean addDefaultEntities = true;


    //-----------/
    //- Methods -/
    //-----------/

    /**
     * Method checkFieldWithDuplicate.
     *
     * @param parser
     * @param parsed
     * @param alias
     * @param tagName
     * @throws XmlPullParserException
     * @return boolean
     */
    public static boolean checkFieldWithDuplicate(XmlPullParser parser, String tagName, String alias, java.util.Set parsed )
            throws XmlPullParserException
    {
        if ( !( parser.getName().equals( tagName ) || parser.getName().equals( alias ) ) )
        {
            return false;
        }
        if ( !parsed.add( tagName ) )
        {
            throw new XmlPullParserException( "Duplicated tag: '" + tagName + "'", parser, null );
        }
        return true;
    } //-- boolean checkFieldWithDuplicate( XmlPullParser, String, String, java.util.Set )

    /**
     * Method checkUnknownAttribute.
     *
     * @param parser
     * @param strict
     * @param tagName
     * @param attribute
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static void checkUnknownAttribute( XmlPullParser parser, String attribute, String tagName, boolean strict )
            throws XmlPullParserException, IOException
    {
        // strictXmlAttributes = true for model: if strict == true, not only elements are checked but attributes too
        if ( strict )
        {
            throw new XmlPullParserException( "Unknown attribute '" + attribute + "' for tag '" + tagName + "'", parser, null );
        }
    } //-- void checkUnknownAttribute( XmlPullParser, String, String, boolean )

    /**
     * Method checkUnknownElement.
     *
     * @param parser
     * @param strict
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static void checkUnknownElement( XmlPullParser parser, boolean strict )
            throws XmlPullParserException, IOException
    {
        if ( strict )
        {
            throw new XmlPullParserException( "Unrecognised tag: '" + parser.getName() + "'", parser, null );
        }

        for ( int unrecognizedTagCount = 1; unrecognizedTagCount > 0; )
        {
            int eventType = parser.next();
            if ( eventType == XmlPullParser.START_TAG )
            {
                unrecognizedTagCount++;
            }
            else if ( eventType == XmlPullParser.END_TAG )
            {
                unrecognizedTagCount--;
            }
        }
    } //-- void checkUnknownElement( XmlPullParser, boolean )

    /**
     * Returns the state of the "add default entities" flag.
     *
     * @return boolean
     */
    public boolean getAddDefaultEntities()
    {
        return addDefaultEntities;
    } //-- boolean getAddDefaultEntities()

    /**
     * Method getBooleanValue.
     *
     * @param s
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return boolean
     */
    public static boolean getBooleanValue( String s, String attribute, XmlPullParser parser )
            throws XmlPullParserException
    {
        return getBooleanValue( s, attribute, parser, null );
    } //-- boolean getBooleanValue( String, String, XmlPullParser )

    /**
     * Method getBooleanValue.
     *
     * @param s
     * @param defaultValue
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return boolean
     */
    public static boolean getBooleanValue( String s, String attribute, XmlPullParser parser, String defaultValue )
            throws XmlPullParserException
    {
        if ( s != null && s.length() != 0 )
        {
            return Boolean.valueOf( s ).booleanValue();
        }
        if ( defaultValue != null )
        {
            return Boolean.valueOf( defaultValue ).booleanValue();
        }
        return false;
    } //-- boolean getBooleanValue( String, String, XmlPullParser, String )

    /**
     * Method getByteValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return byte
     */
    public static byte getByteValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            try
            {
                return Byte.valueOf( s ).byteValue();
            }
            catch ( NumberFormatException nfe )
            {
                if ( strict )
                {
                    throw new XmlPullParserException( "Unable to parse element '" + attribute + "', must be a byte", parser, nfe );
                }
            }
        }
        return 0;
    } //-- byte getByteValue( String, String, XmlPullParser, boolean )

    /**
     * Method getCharacterValue.
     *
     * @param s
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return char
     */
    public static char getCharacterValue( String s, String attribute, XmlPullParser parser )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            return s.charAt( 0 );
        }
        return 0;
    } //-- char getCharacterValue( String, String, XmlPullParser )

    /**
     * Method getDateValue.
     *
     * @param s
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return Date
     */
    public static java.util.Date getDateValue( String s, String attribute, XmlPullParser parser )
            throws XmlPullParserException
    {
        return getDateValue( s, attribute, null, parser );
    } //-- java.util.Date getDateValue( String, String, XmlPullParser )

    /**
     * Method getDateValue.
     *
     * @param s
     * @param parser
     * @param dateFormat
     * @param attribute
     * @throws XmlPullParserException
     * @return Date
     */
    public static java.util.Date getDateValue( String s, String attribute, String dateFormat, XmlPullParser parser )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            String effectiveDateFormat = dateFormat;
            if ( dateFormat == null )
            {
                effectiveDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            }
            if ( "long".equals( effectiveDateFormat ) )
            {
                try
                {
                    return new java.util.Date( Long.parseLong( s ) );
                }
                catch ( NumberFormatException e )
                {
                    throw new XmlPullParserException( e.getMessage(), parser, e );
                }
            }
            else
            {
                try
                {
                    DateFormat dateParser = new java.text.SimpleDateFormat( effectiveDateFormat, java.util.Locale.US );
                    return dateParser.parse( s );
                }
                catch ( java.text.ParseException e )
                {
                    throw new XmlPullParserException( e.getMessage(), parser, e );
                }
            }
        }
        return null;
    } //-- java.util.Date getDateValue( String, String, String, XmlPullParser )

    /**
     * Method getDoubleValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return double
     */
    public static double getDoubleValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            try
            {
                return Double.valueOf( s ).doubleValue();
            }
            catch ( NumberFormatException nfe )
            {
                if ( strict )
                {
                    throw new XmlPullParserException( "Unable to parse element '" + attribute + "', must be a floating point number", parser, nfe );
                }
            }
        }
        return 0;
    } //-- double getDoubleValue( String, String, XmlPullParser, boolean )

    /**
     * Method getFloatValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return float
     */
    public static float getFloatValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            try
            {
                return Float.valueOf( s ).floatValue();
            }
            catch ( NumberFormatException nfe )
            {
                if ( strict )
                {
                    throw new XmlPullParserException( "Unable to parse element '" + attribute + "', must be a floating point number", parser, nfe );
                }
            }
        }
        return 0;
    } //-- float getFloatValue( String, String, XmlPullParser, boolean )

    /**
     * Method getIntegerValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return int
     */
    public static int getIntegerValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            try
            {
                return Integer.valueOf( s ).intValue();
            }
            catch ( NumberFormatException nfe )
            {
                if ( strict )
                {
                    throw new XmlPullParserException( "Unable to parse element '" + attribute + "', must be an integer", parser, nfe );
                }
            }
        }
        return 0;
    } //-- int getIntegerValue( String, String, XmlPullParser, boolean )

    /**
     * Method getLongValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return long
     */
    public static long getLongValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            try
            {
                return Long.valueOf( s ).longValue();
            }
            catch ( NumberFormatException nfe )
            {
                if ( strict )
                {
                    throw new XmlPullParserException( "Unable to parse element '" + attribute + "', must be a long integer", parser, nfe );
                }
            }
        }
        return 0;
    } //-- long getLongValue( String, String, XmlPullParser, boolean )

    /**
     * Method getRequiredAttributeValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return String
     */
    public static String getRequiredAttributeValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s == null )
        {
            if ( strict )
            {
                throw new XmlPullParserException( "Missing required value for attribute '" + attribute + "'", parser, null );
            }
        }
        return s;
    } //-- String getRequiredAttributeValue( String, String, XmlPullParser, boolean )

    /**
     * Method getShortValue.
     *
     * @param s
     * @param strict
     * @param parser
     * @param attribute
     * @throws XmlPullParserException
     * @return short
     */
    public static short getShortValue( String s, String attribute, XmlPullParser parser, boolean strict )
            throws XmlPullParserException
    {
        if ( s != null )
        {
            try
            {
                return Short.valueOf( s ).shortValue();
            }
            catch ( NumberFormatException nfe )
            {
                if ( strict )
                {
                    throw new XmlPullParserException( "Unable to parse element '" + attribute + "', must be a short integer", parser, nfe );
                }
            }
        }
        return 0;
    } //-- short getShortValue( String, String, XmlPullParser, boolean )

    /**
     * Method getTrimmedValue.
     *
     * @param s
     * @return String
     */
    public static String getTrimmedValue( String s )
    {
        if ( s != null )
        {
            s = s.trim();
        }
        return s;
    } //-- String getTrimmedValue( String )

    /**
     * Method nextTag.
     *
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     * @return int
     */
    public static int nextTag( XmlPullParser parser )
            throws IOException, XmlPullParserException
    {
        int eventType = parser.next();
        if ( eventType == XmlPullParser.TEXT )
        {
            eventType = parser.next();
        }
        if ( eventType != XmlPullParser.START_TAG && eventType != XmlPullParser.END_TAG )
        {
            throw new XmlPullParserException( "expected START_TAG or END_TAG not " + XmlPullParser.TYPES[eventType], parser, null );
        }
        return eventType;
    } //-- int nextTag( XmlPullParser )

    /**
     * @see ReaderFactory#newXmlReader
     *
     * @param reader
     * @param strict
     * @throws IOException
     * @throws XmlPullParserException
     * @return Model
     */
    public Model read(Reader reader, boolean strict )
            throws IOException, XmlPullParserException
    {
        XmlPullParser parser = addDefaultEntities ? new MXParser(EntityReplacementMap.defaultEntityReplacementMap) : new MXParser( );

        parser.setInput( reader );


        return read( parser, strict );
    } //-- Model read( Reader, boolean )

    /**
     * @see ReaderFactory#newXmlReader
     *
     * @param reader
     * @throws IOException
     * @throws XmlPullParserException
     * @return Model
     */
    public Model read( Reader reader )
            throws IOException, XmlPullParserException
    {
        return read( reader, true );
    } //-- Model read( Reader )

    /**
     * Method read.
     *
     * @param in
     * @param strict
     * @throws IOException
     * @throws XmlPullParserException
     * @return Model
     */
    public Model read(InputStream in, boolean strict )
            throws IOException, XmlPullParserException
    {
        return read( ReaderFactory.newXmlReader( in ), strict );
    } //-- Model read( InputStream, boolean )

    /**
     * Method read.
     *
     * @param in
     * @throws IOException
     * @throws XmlPullParserException
     * @return Model
     */
    public Model read( InputStream in )
            throws IOException, XmlPullParserException
    {
        return read( ReaderFactory.newXmlReader( in ) );
    } //-- Model read( InputStream )

    /**
     * Method read.
     *
     * @param parser
     * @param strict
     * @throws IOException
     * @throws XmlPullParserException
     * @return Model
     */
    public static Model read( XmlPullParser parser, boolean strict )
            throws IOException, XmlPullParserException
    {
        int eventType = parser.getEventType();
        while ( eventType != XmlPullParser.END_DOCUMENT )
        {
            if ( eventType == XmlPullParser.START_TAG )
            {
                if ( strict && ! "project".equals( parser.getName() ) )
                {
                    throw new XmlPullParserException( "Expected root element 'project' but found '" + parser.getName() + "'", parser, null );
                }
                Model model = parseModel( parser, strict );
                model.setModelEncoding( parser.getInputEncoding() );
                return model;
            }
            eventType = parser.next();
        }
        throw new XmlPullParserException( "Expected root element 'project' but found no element at all: invalid XML document", parser, null );
    } //-- Model read( XmlPullParser, boolean )

    private static Model parseModel(XmlPullParser parser, boolean strict) {
        return new Model();
    }

    /**
     * Sets the state of the "add default entities" flag.
     *
     * @param addDefaultEntities
     */
    public void setAddDefaultEntities( boolean addDefaultEntities )
    {
        this.addDefaultEntities = addDefaultEntities;
    } //-- void setAddDefaultEntities( boolean )
}
