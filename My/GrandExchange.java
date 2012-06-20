/**
 * @author Cakemix
 *
 */
public class GrandExchange {

        private static String HTTP_GRAPH_API = "http://services.runescape.com/m=itemdb_rs/api/graph/";
        private static String HTTP_SEARCH_API = "http://open.tip.it/json/ge-search?term=";
        private static String HTTP_GE_API = "http://open.tip.it/json/ge_single_item?item=";
        private static String HTTP_JSON = ".json";

        /* Price is always in int so it's fine to use \d+ */
        private static String PATTERN_AVERAGE = "\":(\\d+)\\}\\,\"average\"\\:";
        /* ID is always in int so it's fine to use \d+ */
        private static String PATTERN_SEARCH = "\\{\"label\":\"(.*?)\",\"id\":(\\d+),\"category\":\".*?\"}";
        /* */
        private static String PATTERN_GE = "\"(ge_id|name|mark\\_price)\":\"(.*?)\"";

        /**
         * @param name Item name
         * @see getGEItem
         */
        public static GEItem getGItem(String name) {
                return getGEItem(getID(name));
        }

        /**
         * @param id Item ID
         * @return GEItem(String name, int id, String price)
         */
        public static GEItem getGEItem(int id) {
                if (id == -1) {
                        return null;
                }

                String page = null;

                try {
                        page = downloadPage(new URL(HTTP_GE_API + id));

                        if (page == null) {
                                return null;
                        }

                } catch (MalformedURLException e) {
                        return null;
                }

                Pattern pattern = Pattern.compile(PATTERN_GE);
                Matcher matcher = pattern.matcher(page);
                if (matcher.find()) {
                        String[] k = new String[2];
                        int i = 0;
                        while (matcher.find()) {
                                k[i] = matcher.group(2);
                                i++;
                        }
                        return new GEItem(k[0], id, k[1]);
                }

                return null;

        }

        /**
         * @param String
         *                      Search
         * @return
         * @return ItemSearch[] {Name, ID}<br>
         *               Empty if no items were found
         * @see ItemSearch
         */

        public static ArrayList<ItemSearch> search(String search) {
                search = search.replace(" ", "+");
                String page = null;
                ArrayList<ItemSearch> searches = new ArrayList<ItemSearch>();

                if (search.trim().isEmpty()) {
                        return searches;
                }

                try {
                        page = downloadPage(new URL(HTTP_SEARCH_API + search));

                        if (page == null) {
                                return searches;
                        }

                } catch (MalformedURLException e) {
                        return searches;
                }

                Pattern pattern = Pattern.compile(PATTERN_SEARCH);
                Matcher matcher = pattern.matcher(page);

                if (matcher.find()) {
                        while (matcher.find()) {
                                String name = matcher.group(1).replace("\\/", "/");
                                String id = matcher.group(2);
                                searches.add(new ItemSearch(name, new Integer(id)));
                        }
                }

                return searches;
        }

        /**
         * Note: It takes the first result
         * 
         * @param name
         *                      part of item name
         * @return Item id<br>
         *               <tt>-1:</tt> Item were not found
         */

        public static int getID(String name) {
                ArrayList<ItemSearch> search = search(name);
                if (search.size() != 0) {
                        return search.get(0).getID();
                }
                return -1;
        }

        /**
         * @param name Item name
         * @see accuratePrice
         */
        public static int accuratePrice(String name) {
                return accuratePrice((getID(name)));
        }

        /**
         * @param id
         *                      Item ID
         * @return Item price<br>
         *               <tt>-1:</tt> Item not found
         */

        public static int accuratePrice(int id) {
                if (id == -1) {
                        return id;
                }

                String page = null;

                try {
                        page = downloadPage(new URL(HTTP_GRAPH_API + id + HTTP_JSON));

                        if (page == null) {
                                return -1;
                        }

                } catch (MalformedURLException e) {
                        return -1;
                }

                Pattern pattern = Pattern.compile(PATTERN_AVERAGE);
                Matcher matcher = pattern.matcher(page);

                if (matcher.find()) {
                        return new Integer(matcher.group(1));
                }

                return -1;
        }

        /**
         * @author Cakemix
         * 
         */
        private static class GEItem {
                private final String name;
                private final String price;
                private final int id;

                /**
                 * @param name
                 *                      Item name
                 * @param description
                 *                      Item description
                 * @param price
                 *                      Item price
                 */
                public GEItem(String name, int id, String price) {
                        this.name = name;
                        this.id = id;
                        this.price = price;
                }

                /**
                 * @return the name
                 */
                public String getName() {
                        return name;
                }

                /**
                 * @return the price
                 */
                public String getPrice() {
                        return price;
                }

                /**
                 * @return the id
                 */
                public int getId() {
                        return id;
                }

                /*
                 * @see java.lang.Object#toString()
                 */
                @Override
                public String toString() {
                        return "GEItem [getName()=" + getName() + ", getPrice()="
                                        + getPrice() + ", getId()=" + getId() + "]";
                }

        }

        /**
         * @author Cakemix
         * 
         */
        private static class ItemSearch {
                private final String name;
                private final int id;

                /**
                 * @param name
                 *                      Item name
                 * @param id
                 *                      Item id
                 */
                public ItemSearch(String name, int id) {
                        this.name = name;
                        this.id = id;
                }

                /**
                 * @return Item name
                 */
                public String getName() {
                        return name;
                }

                /**
                 * @return Item ID
                 */
                public int getID() {
                        return id;
                }

                /*
                 * @see java.lang.Object#toString()
                 */
                @Override
                public String toString() {
                        return "ItemSearch [getName()=" + getName() + ", getID()="
                                        + getID() + "]";
                }

        }

        /**
         * @param url
         *                      Page url
         * @return Page website source<br>
         *               Null if site is not found
         */

        private static String downloadPage(URL url) {
                try {

                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                        url.openStream()));

                        StringBuilder page = new StringBuilder();

                        String line = null;

                        while ((line = in.readLine()) != null) {
                                page.append(line);
                        }

                        in.close();

                        return page.toString();
                } catch (IOException e) {
                        return null;
                }
        }

}