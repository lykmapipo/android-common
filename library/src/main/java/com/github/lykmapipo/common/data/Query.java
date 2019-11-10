package com.github.lykmapipo.common.data;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;

import com.github.lykmapipo.common.Common;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A generic class that holds Data Query Builder
 * <p>
 *
 * @author lally elias <lallyelias87@gmail.com>
 * @link https://docs.mongodb.com/manual/tutorial/query-documents/
 * @link https://docs.mongodb.com/manual/reference/operator/query/#query-selectors
 * @link https://github.com/mongodb/mongo-java-driver/blob/master/driver-core/src/main/com/mongodb/client/model/Filters.java
 * @link https://github.com/mongodb/mongo-java-driver/blob/master/driver-core/src/main/com/mongodb/client/model/Sorts.java
 * @link https://github.com/RutledgePaulV/q-builders
 * @link https://firebase.google.com/docs/firestore/query-data/queries
 * @link https://github.com/firebase/firebase-android-sdk/blob/master/firebase-database/src/main/java/com/google/firebase/database/Query.java
 * @link https://github.com/firebase/firebase-android-sdk/blob/master/firebase-firestore/src/main/java/com/google/firebase/firestore/Query.java
 * @link https://github.com/firebase/firebase-android-sdk/blob/master/firebase-firestore/src/main/java/com/google/firebase/firestore/core/Query.java
 * @since 0.1.0
 */
public class Query {
    // sort
    public static final Integer SORT_ASC = 1;
    public static final Integer SORT_DESC = -1;
    // query keys
    static final String KEY_SEARCH = "q";
    static final String KEY_PAGE = "page";
    static final String KEY_LIMIT = "limit";
    static final String KEY_FILTER = "filter";
    static final String KEY_FIELDS = "fields";
    static final String KEY_SORT = "sort";
    static final String KEY_POPULATE = "populate";
    // default gson convertor
    private static Gson gson = Common.gson();

    // specify search query condition
    String q;

    // specify page condition
    Long page = 1L;

    // specify limit condition
    Long limit = 10L;

    // specify filter
    Set<Object> filter = new ArraySet<Object>();

    // specify selected fields
    Map<String, Integer> select = new ArrayMap<String, Integer>();

    // specify sort order
    Map<String, Integer> sort = new ArrayMap<String, Integer>();

    /**
     * Instantiate default {@link Query}
     *
     * @return {@link Query}
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Query create() {
        Query query = new Query();
        query.page(1L);
        query.limit(10L);
        return query;
    }

    /**
     * Instantiate new {@link Query}
     *
     * @param page valid page
     * @return {@link Query}
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Query create(@NonNull Long page) {
        Query query = new Query();
        query.page(page);
        query.limit(10L);
        return query;
    }

    /**
     * Instantiate new {@link Query}
     *
     * @param q valid search term
     * @return {@link Query}
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Query create(@NonNull String q) {
        Query query = new Query();
        query.search(q);
        query.limit(10L);
        return query;
    }

    /**
     * Instantiate new {@link Query}
     *
     * @param q    valid search term
     * @param page valid page
     * @return {@link Query}
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Query create(@NonNull String q, @NonNull Long page) {
        Query query = new Query();
        query.search(q);
        query.page(page);
        query.limit(10L);
        return query;
    }

    /**
     * Create default query map
     *
     * @return {@link Map}
     * @since 0.1.0
     */
    @NonNull
    public static synchronized Map<String, String> defaultQueryMap() {
        Query query = create();
        return query.toQueryMap();
    }

    /**
     * Append search query condition
     *
     * @param q valid search query
     * @return {@link Query}
     * @since 0.1.0
     */
    @NonNull
    public Query search(@NonNull String q) {
        this.q = q;
        return this;
    }

    /**
     * Append page query condition
     *
     * @param page valid page number
     * @return {@link Query}
     * @since 0.1.0
     */
    @NonNull
    public Query page(@NonNull Long page) {
        this.page = page;
        return this;
    }

    /**
     * Append limit query condition
     *
     * @param limit valid page number
     * @return {@link Query}
     */
    @NonNull
    public Query limit(@NonNull Long limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Specify sorting order
     *
     * @param field valid field for sorting
     * @param value valid sorting order
     * @return {@link Query}
     * @link https://docs.mongodb.com/manual/reference/method/cursor.sort/
     * @since 0.1.0
     */
    @NonNull
    public Query sort(@NonNull String field, @NonNull Integer value) {
        this.sort.put(field, value);
        return this;
    }

    /**
     * Specify descending sorting order
     *
     * @param field valid field for sorting
     * @return {@link Query}
     * @link https://docs.mongodb.com/manual/reference/method/cursor.sort/
     * @since 0.1.0
     */
    @NonNull
    public Query descBy(@NonNull String field) {
        this.sort(field, SORT_DESC);
        return this;
    }

    /**
     * Specify ascending sorting order
     *
     * @param field valid field for sorting
     * @return {@link Query}
     * @link https://docs.mongodb.com/manual/reference/method/cursor.sort/
     * @since 0.1.0
     */
    @NonNull
    public Query ascBy(@NonNull String field) {
        this.sort(field, SORT_ASC);
        return this;
    }

    /**
     * Specify query conditions using query {@link Filter}
     *
     * @param criteria valid criteria to apply on query
     * @return {@link Query}
     * @link https://docs.mongodb.com/manual/reference/method/cursor.sort/
     * @since 0.1.0
     * <pre>
     * query.filter($eq("age",1));
     * query.filter($or($gte("age",1)));
     * query.filter($and($gte("age",1)));
     * </pre>
     */
    @NonNull
    public Query filter(@NonNull Object... criteria) {
        this.filter.addAll(Common.Value.setOf(criteria));
        return this;
    }


    /**
     * Convert {@link Query} to valid query map for use with api calls or database querying
     *
     * @return {@link Map}
     * @since 0.1.0
     */
    @NonNull
    public Map<String, String> toQueryMap() {
        // initialize query map
        HashMap<String, String> queryMap = new HashMap<String, String>();

        // handle search query condition
        if (!Common.Strings.isEmpty(q)) {
            queryMap.put(KEY_SEARCH, q);
        }

        // handle page condition
        queryMap.put(KEY_PAGE, String.valueOf(page == null ? 1L : page));

        // handle limit condition
        queryMap.put(KEY_LIMIT, String.valueOf(limit == null ? 10L : limit));

        // handle filter
        if (filter != null && !filter.isEmpty()) {
            String queryFilter = gson.toJson(filter);
            queryMap.put(KEY_FILTER, queryFilter);
        }

        // handle sort
        if (sort != null && !sort.isEmpty()) {
            String querySort = gson.toJson(sort);
            queryMap.put(KEY_SORT, querySort);
        }

        // return query map
        return queryMap;
    }

    /**
     * Convert {@link Query} to raw sql query for database querying
     *
     * @return {@link String}
     * @since 0.1.0
     */
    @NonNull
    public String toSQL() {
        return ""; //TODO
    }

    /**
     * A factory for query filters.
     * <pre>
     * and(eq("x", 1), lt("y", 3));
     * or(eq("x", 1), lt("y", 3));
     * </pre>
     *
     * @since 0.1.0
     */
    public static class Filter {
        // comparison operators
        static final String $eq = "$eq";
        static final String $gt = "$gt";
        static final String $gte = "$gte";
        static final String $in = "$in";
        static final String $lt = "$lt";
        static final String $lte = "$lte";
        static final String $ne = "$ne";
        static final String $nin = "$nin";

        // logical operators
        static final String $and = "$and";
        static final String $or = "$or";

        /**
         * Specifies is equal condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/eq/#op._S_eq
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, V>> $eq(@NonNull String field, V value) {
            Map<String, V> condition = Common.Value.mapOf($eq, value);
            Map<String, Map<String, V>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies greater than condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/gt/#op._S_gt
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, V>> $gt(@NonNull String field, V value) {
            Map<String, V> condition = Common.Value.mapOf($gt, value);
            Map<String, Map<String, V>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies greater than or equal condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/gte/#op._S_gte
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, V>> $gte(@NonNull String field, V value) {
            Map<String, V> condition = Common.Value.mapOf($gte, value);
            Map<String, Map<String, V>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies is in condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/in/#op._S_in
         * @since 0.1.0
         */
        public static <V> Map<String, Map<String, Set<V>>> $in(@NonNull String field, V... value) {
            Map<String, Set<V>> condition = Common.Value.mapOf($in, value);
            Map<String, Map<String, Set<V>>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies less than condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/lt/#op._S_lt
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, V>> $lt(@NonNull String field, V value) {
            Map<String, V> condition = Common.Value.mapOf($lt, value);
            Map<String, Map<String, V>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies not equal condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/ne/#op._S_ne
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, V>> $lte(@NonNull String field, V value) {
            Map<String, V> condition = Common.Value.mapOf($lte, value);
            Map<String, Map<String, V>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies equality condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/eq/#op._S_eq
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, V>> $ne(@NonNull String field, V value) {
            Map<String, V> condition = Common.Value.mapOf($ne, value);
            Map<String, Map<String, V>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Specifies is not in condition
         *
         * @param field valid field
         * @param value valid value
         * @return valid equal field criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/nin/#op._S_nin
         * @since 0.1.0
         */
        public static synchronized <V> Map<String, Map<String, Set<V>>> $nin(@NonNull String field, V... value) {
            Map<String, Set<V>> condition = Common.Value.mapOf($nin, value);
            Map<String, Map<String, Set<V>>> criteria = Common.Value.mapOf(field, condition);
            return criteria;
        }

        /**
         * Performs a logical AND operation on an array of one or more expressions
         *
         * @param criterias valid criteria
         * @return valid criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/and/#op._S_and
         * @since 0.1.0
         */
        public static synchronized Map<String, List<Object>> $and(Object... criterias) {
            List<Object> conditions = Common.Value.listOf(criterias);
            Map<String, List<Object>> and = Common.Value.mapOf($and, conditions);
            return and;
        }

        /**
         * Performs a logical OR operation on an array of two or more
         *
         * @param criterias valid criteria
         * @return valid criteria
         * @link https://docs.mongodb.com/manual/reference/operator/query/or/#op._S_or
         * @since 0.1.0
         */
        public static synchronized Map<String, List<Object>> $or(Object... criterias) {
            List<Object> conditions = Common.Value.listOf(criterias);
            Map<String, List<Object>> or = Common.Value.mapOf($or, conditions);
            return or;
        }
    }
}
