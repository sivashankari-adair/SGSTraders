package com.adairtechnology.sgstraders.Util;

/**
 * Created by Android-Team1 on 12/27/2016.
 */

public class EndPoints {
     /*SGS Traders*/

  /*  public static String tdy_wise_update ="http://space7cloud.com/sgs_trader/sgs_datas.php?user_godown_id=";
    public static String update_test ="http://space7cloud.com/sgs_trader/sgs_datas.php?save_godown_id=";//value + date we have to send
    public static String login_check = "http://space7cloud.com/sgs_trader/sgs_datas.php?page=login&username=";
    public static String search_test = "http://space7cloud.com/sgs_trader/sgs_datas.php?page=search&godown_id=";*/

//.192.168.1.100
    public static String host_address    = "http://192.168.1.1/";
    public static String tdy_wise_update = host_address + "sgs_traders/sgs_datas.php?user_godown_id=";
    public static String update_test     = host_address + "sgs_traders/sgs_datas.php?save_godown_id=";//value + date we have to send
    public static String login_check     = host_address + "sgs_traders/sgs_datas.php?page=login&username=";
    public static String search_test     = host_address + "sgs_traders/sgs_datas.php?page=search&godown_id=";
}
