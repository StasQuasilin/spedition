package ua.svasilina.spedition.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;

import ua.svasilina.spedition.entity.Product;
import ua.svasilina.spedition.utils.db.DBHelper;
import ua.svasilina.spedition.utils.db.Tables;

public class ProductsUtil {
    private static final String TAG = "Product Util";
    final  DBHelper helper;
    SQLiteDatabase db;
    private static final HashMap<Integer, Product> defaultProducts = new HashMap<>();
    static {
        addDefaultProduct(new Product(1, "- Олія раф"));
        addDefaultProduct(new Product(2, "- Олія нераф"));
        addDefaultProduct(new Product(3, "- Готова продукція"));
        addDefaultProduct(new Product(4, "- Рафінація"));
    }

    private static void addDefaultProduct(Product product) {
        defaultProducts.put(product.getId(), product);
    }

    public ProductsUtil(Context context) {
        helper = new DBHelper(context);
    }

    public LinkedList<Product> getProducts(){
        LinkedList<Product> products = new LinkedList<>();
        db = helper.getWritableDatabase();
        final Cursor query = db.query(Tables.PRODUCTS, null, null, null, null, null, null);
        if (query.moveToFirst()){
            final int serverIdIdx = query.getColumnIndex("server_id");
            final int nameIdx = query.getColumnIndex("name");
            do {
                final int serverId = query.getInt(serverIdIdx);
                final String name = query.getString(nameIdx);
                products.add(new Product(serverId, name));

            } while (query.moveToNext());
        } else {
            System.out.println("NO SAVED PRODUCTS");
        }
        if (products.size() == 0){
            products.addAll(defaultProducts.values());
        }
        db.close();
        return products;
    }

    public LinkedList<Product> getChildren(Product product){
        LinkedList<Product> products = new LinkedList<>();
        products.add(product);
        return products;
    }

    public Product getProduct(int productId) {
        db = helper.getReadableDatabase();
        final Cursor query = db.query(Tables.PRODUCTS, null, "server_id=?", new String[]{String.valueOf(productId)}, null, null, null);

        if(query.moveToFirst()){
            final int serverIdIdx = query.getColumnIndex("server_id");
            final int nameIdx = query.getColumnIndex("name");
            final int serverId = query.getInt(serverIdIdx);
            final String name = query.getString(nameIdx);
            db.close();
            return new Product(serverId, name);
        } else {
            db.close();
            return defaultProducts.get(productId);
        }
    }
}
