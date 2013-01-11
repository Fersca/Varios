package colores;

public class HSL {
   // A class for encapsulating the hue-saturation-luminosity concept.
   private int hue, sat, lum;

   HSL () { hue = sat = lum = 0; }

   HSL (byte _hue, byte _sat, byte _lum) {
      hue = unsigned256(_hue);
      sat = unsigned256(_sat);
      lum = unsigned256(_lum);
   }

   HSL (int red, int grn, int blu) {
      RGB_to_HSL (red, grn, blu);
   }

   double HSL_to_RGB_Value (double n1, double n2, double h) {
      // Auxiliary to HSL_to_RGB().
      if (h > 360.0)
         h -= 360.0;
      else if (h < 0.0)
         h += 360.0;

      if (h < 60.0)
         return n1 + (n2 - n1) * h / 60.0;
      else if (h < 180.0)
         return n2;
      else if (h < 240.0)
         return n1 + (n2-n1)*(240.0 - h) / 60.0;
      else
         return n1;
   }


   int getHue () { return hue; }

   int getSat () { return sat; }

   int getLum () { return lum; }

   void RGB_to_HSL (double R, double G, double B) {
      // Transforms a given R,G,B triplet to a Hue,Saturation,Luminosity one.
      final int Undefined = -1;
      double H, S, L;
      double delta, minval, maxval;

      minval = Math.min (R, G);
      minval = Math.min (minval, B);

      maxval = Math.max (R, G);
      maxval = Math.max (maxval, B);

      // Calculate Lightness
      L = (maxval + minval) / 2.0;

      if (minval == maxval) {
         S = 0;
         H = Undefined;
      }
      else {
         delta = maxval - minval;

         // Calculate Saturation in the range [0,1]
         S = delta / (L <= 128 ? maxval + minval : 510 - (maxval + minval));

         // Calculate Hue in the range [0, 360]
         if (R == maxval)       // between yellow and magenta [degrees]
            H = 60.0 * (G - B) / delta;
         else if (G == maxval)  // between cyan and yellow
            H = 120.0 + 60.0 * (B - R) / delta;
         else              // B == maxval: between magenta and cyan
            H = 240.0 + 60.0 * (R - G) / delta;
         if (H < 0)
            H += 360.0;
      }
      // Now transform the H, S, L values to the range [0,240] (Hue: [0,239]).
      hue = unsigned256 ((int) (H == Undefined ? Undefined : Math.round (H / 360.0 * 239.0)));
      sat = unsigned256 ((int) (Math.round (S * 240)));
      lum = unsigned256 ((int) (Math.round ((L / 255) * 240)));
   }

   void setHue (int _hue) { hue = unsigned256(_hue); }

   void setSat (int _sat) { sat = unsigned256(_sat); }

   void setLum (int _lum) { lum = unsigned256(_lum); }

   int unsigned256 (int signed) {
      return signed >= 0 ? signed : signed + 256;
   }
}