package cn.sheeva.index.compress;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class InvertIndexCompresser {
    public static List<Long> getDocIdsDeltas(Collection<Long> docIdsSet) {
        LinkedList<Long> compressed = new LinkedList<>();
        long lastId = 0l;
        for (Long docId : docIdsSet) {
            Long delta = docId - lastId;
            compressed.add(delta);
            lastId = docId;
        }
        return compressed;
    }

    public static List<Long> getDocIdsDeltas(List<Long> deltas) {
        long lastId = 0l;
        List<Long> docIds = new LinkedList<>();

        for (Long delta : deltas) {
            long docId = lastId + delta;
            docIds.add(docId);
            lastId = docId;
        }
        return docIds;
    }
    
    public static List<VariableByte> getVariableBytes(List<Long> deltas){
        List<VariableByte> bytes=new LinkedList<>();
        for (Long delta : deltas) {
            bytes.add(new VariableByte(delta));
        }
        return bytes;
    }
}