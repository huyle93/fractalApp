/** 
 * FractalSpecs.java:
 * 
 * A structure that maintains a set of specifications for a FractalBox.
 * This represents a particularly primitive version of the Holder pattern,
 * in which all variables are global, class variables that do not change
 * from instance to instance. 
 *
 */

public class FractalSpecs 
{
   //------ constants that shouldn't change during execution
   public static final double   defaultOffset       = 0;
   public static final double   defaultSizeRatio    = 0.5;
   public static final double   defaultAspectRatio  = 1; 
   public static final int      defaultWidth        = 100;
   public static final int      maxMaxDepth         = 8;
   
   //------- constants that are changed in GUI
   public static int     baseWidth   = defaultWidth;  // width of base rectangle
   public static double  aspectRatio = defaultAspectRatio;  // h to w ratio
   
   public static double  childSizeRatio = defaultSizeRatio;  // child:parent size
   public static double  childOffset = defaultOffset; // child position (as %) 
                                                      // along parent edge.
   public static int     maxDepth    = 1;     // max recursion depth
   public static boolean fill        = true;  // fill rectangle interior
   public static boolean opaqueFill  = true;  // opaque if true 
   
   //----------------- reset() ----------------------------------
   public static void reset()
   {
      baseWidth = defaultWidth;
      aspectRatio = defaultAspectRatio;
      childSizeRatio = defaultSizeRatio;
      childOffset = defaultOffset;
   }
   //------------------ resetAll() ------------------------------
   public static void resetAll()
   {
      maxDepth = 1;
      fill = true;
      opaqueFill = true;
      reset();
   }  
}