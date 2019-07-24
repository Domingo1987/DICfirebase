package dic.firebase;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    // Información de la BBDD
    private static final String DB_NOMBRE = "dicguia.db";
    private static int _version;

    // Nombres de Tablas
    private static final String TABLA_LUGARES = "lugares";

    // Columnas de la tabla Lugares
    private static final String ATT_LUGAR_ID = "lugarId";
    private static final String ATT_LUGAR_NOM = "lugarNom";
    private static final String ATT_LUGAR_DESC = "lugarDesc";
    private static final String ATT_LUGAR_LAT = "lugarLat";
    private static final String ATT_LUGAR_LONG = "lugarLong";

    //SINGLETON
    private static  AdminSQLiteOpenHelper _instancia;

    //Getters y Setters
    public int get_version() {
        return _version;
    }

    public void set_version(int version) {
        _version = version;
    }

    //SINGLETON
    public static AdminSQLiteOpenHelper get_instancia(@Nullable Context context) {
            if (_instancia==null)
            {
                _instancia = new AdminSQLiteOpenHelper(context,null);
            }
            return _instancia;
    }

    //Constructor privado.
   private AdminSQLiteOpenHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory)
    {
        super(context, DB_NOMBRE, factory, _version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearLugares = "CREATE TABLE " + TABLA_LUGARES +
                "(" +
                    ATT_LUGAR_ID + "INTEGER PRIMARY KEY," +
                    ATT_LUGAR_NOM + " TEXT," +
                    ATT_LUGAR_DESC + " TEXT," +
                    ATT_LUGAR_LAT + " DECIMAL(9,6)," +
                    ATT_LUGAR_LONG + " DECIMAL(9,6)" +
                ")";

        db.execSQL(crearLugares);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_LUGARES);
            onCreate(db);
        }
    }
}
