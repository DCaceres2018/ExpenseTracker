package com.example.expensetrackerv01;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper_Java extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gastos.db";
    private static final int DATABASE_VERSION = 2;  // Incrementa la versión de la base de datos
    private static final String TABLE_GASTOS = "gastos";
    private static final String TABLE_CATEGORIAS = "categorias";  // Nueva tabla para categorías

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMPORTE = "importe";
    private static final String COLUMN_CATEGORIA = "categoria";
    private static final String COLUMN_METODO_PAGO = "metodo_pago";
    private static final String COLUMN_SUBCATEGORIA = "subcategoria";
    private static final String COLUMN_FECHA = "fecha";  // Nuevo campo de fecha

    private static final String COLUMN_CATEGORIA_ID = "id";
    private static final String COLUMN_CATEGORIA_NOMBRE = "nombre";  // Campo para nombre de la categoría

    public DataBaseHelper_Java(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGastosTable = "CREATE TABLE " + TABLE_GASTOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMPORTE + " TEXT, " +
                COLUMN_CATEGORIA + " TEXT, " +
                COLUMN_METODO_PAGO + " TEXT, " +
                COLUMN_SUBCATEGORIA + " TEXT, " +
                COLUMN_FECHA + " TEXT)";
        db.execSQL(createGastosTable);

        String createCategoriasTable = "CREATE TABLE " + TABLE_CATEGORIAS + " (" +
                COLUMN_CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORIA_NOMBRE + " TEXT)";
        db.execSQL(createCategoriasTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_GASTOS + " ADD COLUMN " + COLUMN_FECHA + " TEXT");
        }
    }

    public void añadirGasto(String importe, String categoria, String metodoPago, String subcategoria, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMPORTE, importe);
        values.put(COLUMN_CATEGORIA, categoria);
        values.put(COLUMN_METODO_PAGO, metodoPago);
        values.put(COLUMN_SUBCATEGORIA, subcategoria);
        values.put(COLUMN_FECHA, fecha);
        db.insert(TABLE_GASTOS, null, values);
    }

    @SuppressLint("Range")
    public List<Gasto> obtenerListaGastos() {
        List<Gasto> gastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GASTOS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                gasto.setImporte(cursor.getString(cursor.getColumnIndex(COLUMN_IMPORTE)));
                gasto.setCategoria(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIA)));
                gasto.setMetodoPago(cursor.getString(cursor.getColumnIndex(COLUMN_METODO_PAGO)));
                gasto.setSubcategoria(cursor.getString(cursor.getColumnIndex(COLUMN_SUBCATEGORIA)));
                gasto.setFecha(cursor.getString(cursor.getColumnIndex(COLUMN_FECHA)));  // Obtener la fecha
                gastos.add(gasto);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return gastos;
    }

    public void eliminarGasto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GASTOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Función para añadir categoría
    public void añadirCategoria(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORIA_NOMBRE, nombre);
        db.insert(TABLE_CATEGORIAS, null, values);
    }

    // Función para obtener categorías
    @SuppressLint("Range")
    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIAS, new String[]{COLUMN_CATEGORIA_NOMBRE}, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                categorias.add(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORIA_NOMBRE)));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return categorias;
    }

    // Función para eliminar categoría
    public void eliminarCategoria(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORIAS, COLUMN_CATEGORIA_NOMBRE + " = ?", new String[]{nombre});
        db.close();
    }
    public int contarGastos() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_GASTOS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    @SuppressLint("Range")
    public List<Gasto> obtenerGastosPorParametros(String fechaInicio, String fechaFin, String categoria, String metodoPago) {
        List<Gasto> gastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM gastos WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (!fechaInicio.isEmpty()) {
            queryBuilder.append(" AND fecha >= ?");
            args.add(fechaInicio);
        }

        if (!fechaFin.isEmpty()) {
            queryBuilder.append(" AND fecha <= ?");
            args.add(fechaFin);
        }

        if (!categoria.isEmpty()) {
            queryBuilder.append(" AND categoria = ?");
            args.add(categoria);
        }

        if (!metodoPago.isEmpty()) {
            queryBuilder.append(" AND metodo_pago = ?");
            args.add(metodoPago);
        }

        Cursor cursor = db.rawQuery(queryBuilder.toString(), args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getInt(cursor.getColumnIndex("id")));
                gasto.setImporte(cursor.getString(cursor.getColumnIndex("importe")));
                gasto.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
                gasto.setMetodoPago(cursor.getString(cursor.getColumnIndex("metodo_pago")));
                gasto.setSubcategoria(cursor.getString(cursor.getColumnIndex("subcategoria")));
                gasto.setFecha(cursor.getString(cursor.getColumnIndex("fecha")));
                gastos.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return gastos;
    }

}
