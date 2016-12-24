package blue.made.bluegin.asset;

/**
 * Created by Sam Sartor on 12/23/2016.
 */
@FunctionalInterface
public interface LoadFunc<A, S extends AssetSource<A>> {
    public A load(S source, String group, String id) throws Exception;
}
