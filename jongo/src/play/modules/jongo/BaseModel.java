package play.modules.jongo;


import com.alibaba.fastjson.annotation.JSONField;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.MongoId;


import java.util.Date;

/**
 *  数据模型
 */
public abstract class BaseModel {
    // auto
    @MongoId
    @JSONField(serialize = false)
    ObjectId id;
    @JSONField(serialize = false)
    Date _created = new Date();
    @JSONField(serialize = false)
    Date _modified;

    @JSONField(name = "id")
    public String getIdAsStr() {
        return id == null ? null : id.toString();
    }

    @JSONField(serialize = false)
    public ObjectId getId() {
        return id;
    }

    @JSONField(deserialize = false)
    public void setId(ObjectId _id) {
        this.id = _id;
    }

    public void setId(String _id) {
        this.id = new ObjectId(_id);
    }

    /**
     * Get MongoCollection of class
     *
     * @param clazz class
     * @return MongoCollection
     */
    public static MongoCollection getCollection(Class clazz) {
        return JongoPlugin.getJongo().getCollection(clazz.getSimpleName().toLowerCase());
    }

    public static Jongo getJongo() {
        return JongoPlugin.getJongo();
    }

    public static MongoCollection getCollection(String name) {
        return JongoPlugin.getJongo().getCollection(name);
    }

    public void save() {
        this._modified = new Date();
        getCollection(this.getClass()).save(this);
    }

    public void remove() {
        getCollection(this.getClass()).remove(this.getId());
    }

}
