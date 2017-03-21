/**
 * FractalPanel - This is a draw panel whose only responsibility is to 
 *                invoke the draw method of the current FractalBox object.
 * @author rdb
 * 02/22/2010
 */

import java.awt.*;
import javax.swing.*;

public class FractalPanel extends JPanel
{
   //------------------- instance variables --------------------------
   private FractalBox _shape;
   
   //-------------------- setShape -----------------------------------
   public void setShape( FractalBox s )
   {
      _shape = s;
   }
   //-------------------- paintComponent -----------------------------
   public void paintComponent( Graphics brush )
   {
      super.paintComponent( brush );
      if ( _shape != null )
         _shape.display( (Graphics2D) brush );
   }
}