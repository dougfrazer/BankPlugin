

import java.util.LinkedList;
import java.util.Iterator;

public class ItemCache {
    
    private LinkedList<BankItem> cache;
    private int[] code_to_index;

    public ItemCache() {
        BankDB database = new BankDB();
        cache = database.total_item_list();
        code_to_index = new int[database.max_code()+1];
        for(Iterator iter = cache.iterator(); 
            iter.hasNext();) {
            BankItem b = (BankItem)iter.next();
            code_to_index[b.code] = b.item_id;
        }
    }

    public String getItemName(int code) {
        // the get() function is 0-based so we need to subtact 1
        return cache.get(code_to_index[code]-1).item_string;
    }


}
