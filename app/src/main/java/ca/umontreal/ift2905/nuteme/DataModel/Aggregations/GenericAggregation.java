package ca.umontreal.ift2905.nuteme.DataModel.Aggregations;

import java.util.List;

/**
 * Created by h on 11/04/16.
 */
public class GenericAggregation<T> {

    public GenericAggregation(List<T> aggregationList) {
        this.aggregationList = aggregationList;
    }

    public List<T> aggregationList;
}
