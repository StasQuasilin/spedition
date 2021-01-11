package ua.svasilina.spedition.utils.builders;

import android.content.res.Resources;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.entity.Weight;

import static ua.svasilina.spedition.constants.Keys.COMA;
import static ua.svasilina.spedition.constants.Keys.HYPHEN;
import static ua.svasilina.spedition.constants.Keys.SPACE;

public class WeightStringBuilder {
    private final Resources resources;

    public WeightStringBuilder(Resources resources) {
        this.resources = resources;
    }

    public String build(Weight weight) {
        return resources.getString(R.string.B) +
                SPACE +
                weight.getGross() +
                COMA + SPACE +
                resources.getString(R.string.T) +
                SPACE +
                weight.getTare() +
                COMA + SPACE +
                resources.getString(R.string.N) +
                SPACE +
                weight.getNet();
    }
    public String buildShort(Weight weight){
        final int gross = weight.getGross();
        final int tare = weight.getTare();
        if (gross > 0 && tare > 0){
            return resources.getString(R.string.N) +
                    SPACE +
                    weight.getNet();
        } else if (gross > 0){
            return resources.getString(R.string.B) +
                    SPACE +
                    gross;
        } else if(tare > 0){
            return resources.getString(R.string.T) +
                    SPACE +
                    tare;
        } else {
            return resources.getString(R.string.N) +
                    SPACE + HYPHEN;
        }
    }
}
