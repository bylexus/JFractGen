package ch.alexi.jfractgen.logic;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import ch.alexi.jfractgen.models.FractParam;

/**
 * The ObjectFactory class ist responsible for creating several different needed
 * objects - In Absence of a real dependency injection mechanism, I stick with
 * this simple solution.
 */
public class ObjectFactory {

    protected Map<IColorizerStrategy.COLORIZER_STRATEGY, Colorizer> colorizersByStrategy = new HashMap<>();

    public Colorizer createColorizer(final FractParam params) throws NoSuchAlgorithmException {
        final IColorizerStrategy.COLORIZER_STRATEGY strategyKey = params.fixedSizePalette == true
                ? IColorizerStrategy.COLORIZER_STRATEGY.ROTATING
                : IColorizerStrategy.COLORIZER_STRATEGY.PERCENTAGE;
        if (colorizersByStrategy.containsKey(strategyKey) != true) {
            IColorizerStrategy strategy;
            switch(strategyKey) {
                case PERCENTAGE:  strategy = new PercentagePaletteColorizerStrategy(); break;
                case ROTATING:  strategy = new RotatingPaletteColorizerStrategy(); break;
                default: throw new NoSuchAlgorithmException();
            }
            colorizersByStrategy.put(strategyKey, new Colorizer(strategy));
        }
        return colorizersByStrategy.get(strategyKey);
    }
}
