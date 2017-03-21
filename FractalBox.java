/** 
 * FractalBox.java:
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class FractalBox extends Rectangle2D.Float 
{
    //---------------- class variables ------------------------------
    
    //---------------- instance variables ------------------------------
    private FractalBox[]   _children;
    private int            _myDepth;
    private Color[] c;
    private FractalSpecs spec;
    
    //------------------------ constructors ----------------------------
    /**
     *   constructor must recursively create children
     */
    public FractalBox( int depth )
    {
        _myDepth = depth;      
        //***************************************************************
        //  Need to create the 4 children unless depth has reached the 
        //    max specified in the FractalSpec class
        //  
        //***************************************************************
        spec = new FractalSpecs();
        c = new Color[]{ Color.BLUE, Color.CYAN, Color.ORANGE, 
            Color.RED, Color.BLACK, Color.GRAY, 
            Color.YELLOW, Color.GREEN, Color.PINK };
        
        if( _myDepth >= spec.maxDepth )
        {
            return;
        }
        else
        {
            _children = new FractalBox[4];
            for( int i = 0; i < 4; i++ )
            {
                _children[i] = new FractalBox( depth + 1 );
            }
        }
    }
    //------------------- propagate() -----------------------------------
    /** 
     * this method (or one like it) initiates the recursive update of
     * location and sizes of all the FractalShapes beginning with this one.
     * 
     * Don't do it all here, though. Make helper methods to keep the code
     * simpler.
     */
    public void propagate()
    {
        //**********************************************************
        //   need to compute location/size info for the children recursively
        //**********************************************************
        this.goTo( _myDepth + 1 );
    }
    public void goTo( int depth )   
    {
        if (  _myDepth >= FractalSpecs.maxDepth )
        {
            return;
        }
        
        depth = _myDepth;
        
        double csr = FractalSpecs.childSizeRatio;  
        double w = FractalSpecs.baseWidth * ( Math.pow ( csr, _myDepth - 1 ) );
        double off =  FractalSpecs.childOffset;
        double r = FractalSpecs.aspectRatio;
        double h = w * r;
        
        _children[0].setFrame( x + off * w, y - csr * h, csr * w, csr * h );
        _children[1].setFrame( x + w, y + off * h, csr * w, csr * h );
        _children[2].setFrame( x - w * off  + w / 2, y + h, csr * w, csr * h );
        _children[3].setFrame( x - csr * w, y  + h / 2 - off * h, csr * w, csr * h );
        
        _children[0].goTo( depth + 1 );
        _children[1].goTo( depth + 1 );
        _children[2].goTo( depth + 1 );
        _children[3].goTo( depth + 1 );
    }
    
    
    
    //--------------------- display( Graphics2D ) -----------------------
    /**
     * method called by FractalGUI.paintComponent
     */   
    public void display( Graphics2D context ) 
    {
        Color saveColor = context.getColor();
        Color myColor = c[ _myDepth - 1 ];
        //////////////////////////////////////////////////////////////
        // Actual color needs to be determined by depth, so will proably want
        //   an array of colors
        //////////////////////////////////////////////////////////////
        // To implement the semi-transparent option, once you've chosen
        //   an opaque color based on depth, you can use the color values
        //   from that object to create a new color object with the same
        //    RGB, but with an A (alpha) of 0.5 or lower.
        ///////////////////////////////////////////////////////////////
        // draw myself
//      context.setColor( myColor );
//      context.fill( this );
        if( FractalSpecs.opaqueFill == false )
        {
            myColor = new Color( myColor.getRed(),myColor.getGreen(), 
                                myColor.getBlue(), myColor.getAlpha() - 160 );
        }
        context.setColor( myColor );
        if( FractalSpecs.fill == true )
        {
            context.fill( this );
        }
        context.draw( this );
        if( _children != null )
        {
     
            for( int i = 0; i < _children.length ; i++ )
            {
                _children[i].display( context );
                context.setColor( c[_myDepth - 1 ] );
            }
        }
        else
        {
            return;
        }
    }
    ////////////////////////////////////////////////////////////////
    // Need to recursively draw all children
    //
    // Each depth should have a different color.
    ///////////////////////////////////////////////////////////////
    
    
    // context.setColor( saveColor );
    
}