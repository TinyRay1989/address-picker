# address-picker

####工程目录说明

address-picker  
\---crawler(爬虫,在http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/  爬数据)  
\---rest-api(用crawler收集的数据提供restfulAPI)  
\---src(地址选择器插件代码)



使用方法
~~~
<!DOCTYPE HTML>
<html lang="zh-cn">
<head>
	<meta charset="utf-8">
	<link type="text/css" rel="stylesheet" href="css/address-picker.css">
</head>
<body>
	<div style="margin-top:100px;">
        <input id="btn1" value="关闭第一个" type="button"/>
        <input id="id1"/><br>
        <br>
        <br>
        <br>
        <input id="btn2" value="保存第二个" type="button"/>
        <input id="id2"/><br>
	</div>
</body>

<script src="../../address-picker/src/js/address-picker-data.min.js"></script>
<script src="../../address-picker/src/js/address-picker.js"></script>
<script>
var addressPicker1 = new AddressPicker();
var addressPicker2 = new AddressPicker();
addressPicker1.init({
    'trigger' : '#id1',
    data : addressPickerData,
    separator : '*'
});
document.getElementById('btn1').onclick = function() {
    addressPicker1.close();
};

addressPicker2.init({
    'trigger' : '#id2',
    autoSave : false,
    isAjax : true,
    url : "http://localhost",
    callback : function(info){
        alert(info.getDesc());
    }
});
document.getElementById('btn2').onclick = function() {
    addressPicker2.save();
};
</script>
</html>

~~~
