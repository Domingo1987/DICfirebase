package dic.firebase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Lugar {

    private int _id;
    private String _nom;
    private String _desc;
    private float _lat;
    private float _long;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_nom() {
        return _nom;
    }

    public void set_nom(String _nom) {
        this._nom = _nom;
    }

    public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
    }

    public float get_lat() {
        return _lat;
    }

    public void set_lat(float _lat) {
        this._lat = _lat;
    }

    public float get_long() {
        return _long;
    }

    public void set_long(float _long) {
        this._long = _long;
    }

    public Lugar(int _cod, String _nom, String _desc, float _lat, float _long) {
        this._id = _cod;
        this._nom = _nom;
        this._desc = _desc;
        this._lat = _lat;
        this._long = _long;
    }

    public Lugar(int _cod) {
        this._id = _cod;
    }

    public void registrar(Lugar lugar, android.content.Context context)
    {
        if (lugar.get_id()>0 && !lugar.get_nom().isEmpty())
        {
            AdminSQLiteOpenHelper admin = AdminSQLiteOpenHelper.get_instancia(context);
            SQLiteDatabase base = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();
            registro.put("lugarId", lugar.get_id());
            registro.put("lugarNom", lugar.get_nom());
            registro.put("lugarDesc", lugar.get_desc());
            registro.put("lugarLat", lugar.get_lat());
            registro.put("lugarLong", lugar.get_long());

            base.insert("lugares", null, registro);

            base.close();
        }
    }

    public Lugar buscar(int id, android.content.Context context)
    {
        if (id>0)
        {
            AdminSQLiteOpenHelper admin = AdminSQLiteOpenHelper.get_instancia(context);
            SQLiteDatabase base = admin.getReadableDatabase();

            Cursor fila = base.rawQuery("select lugarNom, lugarDesc, lugarLat, lugarLong from lugares where=" + id, null);

            if (fila.moveToFirst())
            {

                String nombre = fila.getString(0);
                String descripcion = fila.getString(1);
                float latitud = fila.getFloat(2);
                float longuitud = fila.getFloat(3);


                fila.close();
                base.close();

                return new Lugar(id, nombre, descripcion, latitud, longuitud);
            }
            else
            {
                fila.close();
                base.close();
                return new Lugar(-1);
            }



        }
        else
        {
            return new Lugar(-1);
        }
    }
}
