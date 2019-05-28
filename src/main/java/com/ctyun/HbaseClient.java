package com.ctyun;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

public class HbaseClient {
    
    private volatile static Admin admin;
    private volatile static Configuration conf;
    
    private Configuration getConf() {
        if(conf == null) {
            synchronized(HbaseClient.class) {
                if(conf == null) {
                    conf=HBaseConfiguration.create();
                    //zookeeper地址
                    conf.set("hbase.zookeeper.quorum","127.0.0.1");
                }
            }
        }
        return conf;
    }
    
    private Connection getConnection() throws IOException {
        Connection connection=ConnectionFactory.createConnection(getConf());
        return connection;
    }
    
    private Admin getAdmin() throws IOException {
        if(admin == null) {
            synchronized(HbaseClient.class) {
                if(admin == null) {
                    admin = getConnection().getAdmin();
                }
            }
        }
        return admin;
    }
    
    public void putData(String tableName, String colFamily, String columnName, String data) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf("test-hbase"));
        Put put = new Put(Bytes.toBytes(1));
        put.setDurability(Durability.SKIP_WAL);
        put.addColumn(Bytes.toBytes(colFamily),
                // 列
                Bytes.toBytes(columnName),
                // 列的值
                Bytes.toBytes(data));
        table.put(put);
    }
    
    public void getData(String tableName, String colFamily, String columnName) throws IOException {
        Table table = getConnection().getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(1));
        get.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(columnName));
        Result rs = table.get(get);
        String name = null;
        String num = null;
        String fee = null;
        for(Cell cell: rs.listCells()) {
//            if (Bytes.toString(CellUtil.cloneQualifier(cell)).equals("name")){
//                name = Bytes.toString(CellUtil.cloneValue(cell));
//            }
//            if (Bytes.toString(CellUtil.cloneQualifier(cell)).equals("num")){
//                num = Bytes.toString(CellUtil.cloneValue(cell));
//            }
//            if (Bytes.toString(CellUtil.cloneQualifier(cell)).equals("fee")){
//                fee = Bytes.toString(CellUtil.cloneValue(cell));
//            }
            System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
        }
//        System.out.println(name+":"+num+":"+fee);
    }
    
    public static void main(String[] args) throws IOException {
        new HbaseClient().getData("test-hbase", "llop", "llop2");
    }
    
}