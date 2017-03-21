/**
 * FractalGUI -- based on the GUI class for the canonical 416 application.
 *               This version presents the GUI for the rectangle fractal 
 *               application program
 * 
 * rdb
 * 
 */
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FractalGUI extends JPanel 
{
   //---------------- class variables ------------------------------
    //------------- constants
   
  
   //--------------- instance variables ----------------------------
   
   private FractalBox   _shape;
   private FractalPanel _drawPanel;
   private FractalSpecs FractalSpecs;
   // sliders
   private LabeledSlider _childSizeSlider;
   private LabeledSlider _childOffsetSlider;
   private LabeledSlider _widthSlider;
   private LabeledSlider _hwRatioSlider;

   //------------------ constructor ---------------------------------
   /**
    * Construct the GUI components and the first fractal rectangle
    */
   public FractalGUI()     
   {
      this.setLayout( new BorderLayout() );
      FractalSpecs.maxDepth = 1;         // start at level 1 for debugging
      buildGUI();
      
      _drawPanel = new FractalPanel();
      _drawPanel.setLayout( null );
      this.add( _drawPanel, BorderLayout.CENTER );
   }
   //--------------------- makeShape() -----------------------------
   /**
    * generate the fractal shape based on current parameters
    *    1) the task of creating the 4 children AND their children
    *       and so on (the constructor)
    *    2) the task of determining the locations and sizes of 
    *       everybody (propagate)
    */
   private void makeShape()
   { 
      int w = FractalSpecs.baseWidth;
      int h = (int) ( w * FractalSpecs.aspectRatio );
      
      int x = ( (int)_drawPanel.getWidth() - w ) / 2;
      int y = ( (int)_drawPanel.getHeight() - h ) / 2;
      
      _shape = new FractalBox( 1 );
      _shape.setFrame( x, y, w, h );
      _shape.propagate();
      
      _drawPanel.setShape( _shape );
      this.repaint();
   }
   //--------------------- buildGUI() ---------------------------------------
   /**
    * create all the GUI components
    */
   private void buildGUI()
   {
      JPanel sliders = new JPanel();
      sliders.setBorder( new LineBorder( Color.BLACK, 2 ));
      sliders.setLayout( new GridLayout( 0, 2 ));
      
      buildSliders( sliders );
      add( sliders, BorderLayout.SOUTH );
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setBorder( new LineBorder( Color.BLACK, 2 ));
      buildButtons( buttonPanel );
            
      add( buttonPanel, BorderLayout.NORTH );
   }
   //--------------------- buildSliders( Container ) ------------------------
   /**
    * create the sliders in the Container passed as a parameter
    */
   private void buildSliders( Container sliders )
   {   
      //------------- Child ratio Slider  ------------------------------------
      _childSizeSlider = new LabeledSlider( "Child ratio %", 25, 75, 50 );
      _childSizeSlider.addChangeListener( new SizeRatioListener() );
      sliders.add( _childSizeSlider );
 
      //------------- RelPosition Slider  --------------------------------
      _childOffsetSlider = new LabeledSlider( "Relative position %", -100, 150, 0 );
      _childOffsetSlider.addChangeListener( new PositionListener() );
      sliders.add( _childOffsetSlider );
 
      //------------- BaseWidth Slider  ------------------------------------
      _widthSlider = new LabeledSlider( "Base Width", 50, 200, 75 );
      _widthSlider.addChangeListener( new WidthListener() );
      sliders.add( _widthSlider );

      //------------- Aspect ratio Slider  ------------------------------------
      _hwRatioSlider = new LabeledSlider( "H/W ratio %", 25, 400, 100 );
      _hwRatioSlider.addChangeListener( new AspectRatioListener() );
      sliders.add( _hwRatioSlider );     
   }
   //--------------------- buildButtons( Container ) ------------------------
   /**
    * create the buttons and spinner in the Container passed as a parameter
    */
   private void buildButtons( Container buttonPanel )
   {      
      LabeledSpinner depthSpinner = new LabeledSpinner( 
                           "Recursion depth", 1, FractalSpecs.maxMaxDepth, 1 );
      depthSpinner.addChangeListener( new DepthListener() );
      buttonPanel.add( depthSpinner );
                 
      JRadioButton rButton = new JRadioButton( "Fill" ); 
      rButton.addActionListener( new FillListener() );
      rButton.setSelected( true );
      buttonPanel.add( rButton );
                 
      rButton = new JRadioButton( "Opaque" ); 
      rButton.addActionListener( new SolidFillListener() );
      rButton.setSelected( true );
      buttonPanel.add( rButton );
            
      JButton button = new JButton( "Reset parameters" );
      button.addActionListener( new ResetListener() );
      button.setSelected( false );
      buttonPanel.add( button );
   }
   //-------------------- paintComponent -----------------------------
   /**
    * By not creating the fractal shape until the first call to this
    * paintComponent, the windows are all madke when we do create the
    * box and so it gets properly placed in the center of the screen.
    */
   public void paintComponent( Graphics brush )
   {
      super.paintComponent( brush );
      if ( _shape == null )
         makeShape();
   }
  
   //--------------------- reset() ------------------------------------------
   /**
    * reset all generation parameters to their initial values (except for 
    * the spinner.
    */
   private void reset()
   {
      FractalSpecs.reset();
      _childSizeSlider.getJSlider().setValue( 
                     (int) Math.round( FractalSpecs.childSizeRatio * 100 ));
      _childOffsetSlider.getJSlider().setValue( 
                     (int) Math.round( FractalSpecs.childOffset * 100 ) );
      _widthSlider.getJSlider().setValue( FractalSpecs.baseWidth );
      _hwRatioSlider.getJSlider().setValue( 
                     (int) Math.round( FractalSpecs.aspectRatio * 100 ));
      makeShape();      
   }
 
   
   //++++++++++++++++++ inner classes ++++++++++++++++++++++++++++++++++
   /**
    * WidthListener inner class sets baseWidth parameter
    */
   public class WidthListener implements ChangeListener
   {
      //------- stateChanged-------------
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalSpecs.baseWidth = slider.getValue();
         makeShape();
      }
   }
   /**
    * SizeRatioListener inner class sets childSizeRatio parameter
    */
   public class SizeRatioListener implements ChangeListener
   {
      //------- stateChanged-------------
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalSpecs.childSizeRatio = slider.getValue() / 100.0;
         makeShape();
      }
   }
   /**
    * AspectRatioListener inner class
    */
   public class AspectRatioListener implements ChangeListener
   {
      //------- stateChanged-------------
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalSpecs.aspectRatio = slider.getValue() / 100.0;
         makeShape();
      }
   }
   /**
    * PositionListener inner class -- sets childOffset parameter
    */
   public class PositionListener implements ChangeListener
   {
      //------- stateChanged-------------
      public void stateChanged( ChangeEvent ev )
      {
         JSlider slider = (JSlider) ev.getSource();
         FractalSpecs.childOffset = slider.getValue() / 100.0;
         makeShape();
      }
   }
   /**
    * DepthListener inner class -- sets maxDepth parameter
    */
   public class DepthListener implements ChangeListener
   {
      //------- stateChanged-------------
      public void stateChanged( ChangeEvent ev )
      {
         JSpinner spinner = (JSpinner) ev.getSource();
         Number spinValue = (Number) spinner.getValue();
         FractalSpecs.maxDepth = spinValue.intValue();
         makeShape();
      }
   }
   //+++++++++++++++++++++++++++ FillListener class +++++++++++++++++++++++++
   /**
    * responds to an event from a JRadioButton that identifies whether 
    * to fill the triangles or just draw outlines
    */
   private class FillListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         FractalSpecs.fill = !FractalSpecs.fill;
         makeShape();
      }
   }

   //+++++++++++++++++++++++++++ SolidFillListener class +++++++++++++++++++++
   /**
    * responds to an event from a JRadioButton that identifies whether 
    * to fill the triangles with opaque colors; otherwise uses semi-transparent
    */
   private class SolidFillListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         FractalSpecs.opaqueFill = !FractalSpecs.opaqueFill;
         makeShape();
      }
   }

   //+++++++++++++++++++++++++++ ResetListener class +++++++++++++++++++++++++
   /**
    * responds to an event from a JButton that identifies that the generation
    * parameters should be reset to their initial values
    */
   private class ResetListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         reset();
      }
   }
   //------------------------------- main -------------------------------
   public static void main( String [] args ) 
   {
      FractalApp app = new FractalApp( "FractalApp", args );
   }
}