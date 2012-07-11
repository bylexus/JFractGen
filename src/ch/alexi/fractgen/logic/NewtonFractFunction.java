package ch.alexi.fractgen.logic;

import ch.alexi.fractgen.models.FractFunctionResult;

/**
 * An implementing algorithm for the fractal function: Newton Fractal.
 * 
 * The Newton set is defined by:
 * 
 * Z(n+1) = Z(n) - (p(z(n) / p'(z(n)))
 * 
 * while p(z) = z^3-1 and p'(z) = 3*z^2
 * 
 * while Z(0) = (cx + (cy)i); 
 * cx = initial real value, calculated from the actual pixel's x position
 * cy = initial imaginary value, calculated from the actual pixel's y position
 * 
 * The number is iterated as long as it is clear that is is not converging towards 0.
 * 
 * Part of JFractGen - a Julia / Mandelbrot Fractal generator written in Java/Swing.
 * @author Alexander Schenkel, www.alexi.ch
 * (c) 2012 Alexander Schenkel
 */
public class NewtonFractFunction implements IFractFunction {
	public String toString() {
		return "Newton";
	}

	@Override
	public FractFunctionResult fractIterFunc(double cx, double cy, double max_betrag_quadrat,
			double max_iter, double julia_r, double julia_i) {
		double threshold = 0.001;
		double result = 1;
		double iter = 0;
		double x = cx,xt;
		double y = cy,yt;

		/**
		 * Z(n+1)  = Z(n) - (p(z(n) / p'(z(n)))
		 * (xt,yt) = (x + yi) - ((x+yi)^3-1) / (3*(x+yi)^2)
		 *         = x + yi - (x^3 + 3*x^2*yi + 3*x*yi^2 + yi^3 -1) / (3* (x^2 + 2*x*yi+yi^2))
		 *         = x + yi - (x^3 + 3*x^2*yi - 3*x*y^2 - y^3i -1) / (3* (x^2 + 2*x*yi -y))
		 */
		/**
		 * A python example:
		 * http://code.activestate.com/recipes/577166-newton-fractals/
		 * # Newton fractals
# FB - 201003291
from PIL import Image
imgx = 512
imgy = 512
image = Image.new("RGB", (imgx, imgy))

# drawing area
xa = -1.0
xb = 1.0
ya = -1.0
yb = 1.0

maxIt = 20 # max iterations allowed
h = 1e-6 # step size for numerical derivative
eps = 1e-3 # max error allowed

# put any complex function here to generate a fractal for it!
def f(z):
    return z * z * z - 1.0

# draw the fractal
for y in range(imgy):
    zy = y * (yb - ya) / (imgy - 1) + ya
    for x in range(imgx):
        zx = x * (xb - xa) / (imgx - 1) + xa
        z = complex(zx, zy)
        for i in range(maxIt):
            # complex numerical derivative
            dz = (f(z + complex(h, h)) - f(z)) / complex(h, h)
            z0 = z - f(z) / dz # Newton iteration
            if abs(z0 - z) < eps: # stop when close enough to any root
                break
            z = z0
        image.putpixel((x, y), (i % 4 * 64, i % 8 * 32, i % 16 * 16))

image.save("newtonFr.png", "PNG")

		Complex nrs i java:
		http://xahlee.info/java-a-day/ex_complex.html
		 */
		while (result > threshold && iter < max_iter) {
			
			
			
			xt = x * x - y*y + julia_r;
			yt = 2*x*y + julia_i;
			x = xt;
			y = yt;
			iter += 1;
			result = x*x + y*y;
		}
		FractFunctionResult r = new FractFunctionResult();
		r.iterValue = iter;
		r.bailoutValue = result;
		return r;
	}
}
