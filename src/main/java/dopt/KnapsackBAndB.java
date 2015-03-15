package dopt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.TreeMultimap;

public class KnapsackBAndB extends Knapsack {

    static class Item {
        Item(int idx, int weight, int value) {
            this.idx = idx;
            this.weight = weight;
            this.value = value;
        }

        final int idx;
        final int weight;
        final int value;
        @Override
        public String toString() {
            ToStringHelper helper = MoreObjects.toStringHelper(Item.class);
            helper.add("idx", idx);
            helper.add("weight", weight);
            helper.add("value", value);
            return helper.toString();
        }
    }

    Result calcBest(List<Item> items, int p, int capacity, double maxPossibleValue, double bestSeenValue, int capacityLeft) {
        if(bestSeenValue > maxPossibleValue){
            return new Result(capacity);
        }
        
        if (items.size() - 1 == p) {
            Item item = items.get(p);
            if (capacityLeft >= item.weight) {
                Result r = new Result(capacity);
                r.points[item.idx] = 1;
                r.value = item.value;
                return r;
            }
            return new Result(capacity);
        }

        Result bestSoFar = new Result(capacity);
        
        for (int i = p; i < items.size(); i++) {
            Item itm = items.get(i);
            Result bestThisIter = null;
            if (capacityLeft >= itm.weight) {
                
                Result selected = calcBest(items, i + 1, capacity, maxPossibleValue, bestSoFar.value, capacityLeft - itm.weight);
                selected.value += itm.value;
                selected.points[itm.idx] = 1;
                
                Result dropped = calcBest(items, i + 1, capacity, maxPossibleValue - itm.value, bestSoFar.value, capacityLeft);

                bestThisIter= (selected.value > dropped.value) ? selected : dropped;
                
            } else {
                bestThisIter = calcBest(items, i + 1, capacity, maxPossibleValue - itm.value, bestSoFar.value, capacityLeft);
            }
            if(bestThisIter.value > bestSoFar.value){
                bestSoFar = bestThisIter;
            }
        }
        return bestSoFar;
    }

    @Override
    Result run(Problem pr) throws URISyntaxException, IOException {

        List<Item> items = new ArrayList<>();

        for (int i = 0; i < pr.count; i++) {
            items.add(new Item(i, pr.weights[i], pr.values[i]));
        }
        double maxValue = maxValuePossible(pr);

        return calcBest(items, 0, pr.count, maxValue, 0.0, pr.capacity);
    }

    private double maxValuePossible(Problem pr) {
        TreeMultimap<Double, Integer> m = TreeMultimap.create();
        for (int i = 0; i < pr.count; i++) {
            m.put((double) pr.weights[i] / pr.values[i], i);
        }

        double maxValue = 0.0;
        int capacity = 0;
        for (Entry<Double, Integer> i : m.entries()) {
            if (capacity > pr.capacity) {
                break;
            }
            int ii = i.getValue();
            int w = pr.weights[ii];
            int v = pr.values[ii];

            if (w + capacity > pr.capacity) {
                maxValue += v * (pr.capacity - capacity) / w;
                break;
            }
            maxValue += v;
            capacity += w;
        }
        return maxValue;
    }

}
