package ca.umontreal.ift2905.nuteme.DataModel.Aggregations;

import java.util.List;

/**
 * Created by h on 11/04/16.
 */
public abstract class GenericAggregationItem<T> {
    public String name;
    public float amount;
    public String unit;
    public List<T> aggregatedList;
}
