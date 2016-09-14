/**
 * 
 */
var order_dialog;
var vdc_db;
var policy_db;
var computingpolicy_db;

order_dialog = $( "#order_dialog" ).dialog({
	autoOpen: false,
    resizable: false,
    //height:600,
    //width:600,
    dialogClass: "panel panel-green",
    buttons: {
      "确认": function() {
        $( this ).dialog( "close" );
        //cleanaci();
        
      },
      Cancel: function() {
        $( this ).dialog( "close" );
      }
    },
    open: function () {
    	$.ajax({
			  crossDomain: true,
			  type:"GET",
			  //headers: { 'Access-Control-Allow-Origin': '*' },
			  url: "http://10.63.51.22:8080/ucsdportal/vdclist"
			  //dataType:"json",
			  //data: {"url":"http://10.63.51.133/app/api/rest?ormatType=json&opName=userAPIGetAllCatalogs"},
			 // username:"root"
			}).done(function(data) {
			    vdc_db=data.serviceResult.rows;
				console.log(vdc_db.length);
				var i,j=0;
				var temp = [];
				var flag=0;
				temp.push(vdc_db[0].Customer_Organizations);
				for(i=1;i<vdc_db.length;i++) {
					flag =0;
					for(j=0;j<temp.length;j++){
						if(temp[j] == vdc_db[i].Customer_Organizations) {
							flag =1;
							break;
						}
					}
					if(flag == 0) temp.push(vdc_db[i].Customer_Organizations);
				}
				
				for(i=0;i<temp.length;i++) {
					$("#id_usergroup").append('<option val="' + temp[i] + '">' + temp[i] + '</option>');
				}
				
				$.ajax({
					  //crossDomain: true,
					  type:"GET",
					  //headers: { 'Access-Control-Allow-Origin': '*' },
					  url: "http://10.63.51.22:8080/ucsdportal/policy"
					  //dataType:"json",
					  //data: {"url":"http://10.63.51.133/app/api/rest?ormatType=json&opName=userAPIGetAllCatalogs"},
					 // username:"root"
					}).done(function(data) {
					   policy_db=data.serviceResult.rows;
					   
					   $.ajax({
							  //crossDomain: true,
							  type:"GET",
							  //headers: { 'Access-Control-Allow-Origin': '*' },
							  url: "http://10.63.51.22:8080/ucsdportal/computingpolicy",
							  dataType: "xml"
							  //dataType:"json",
							  //data: {"url":"http://10.63.51.133/app/api/rest?ormatType=json&opName=userAPIGetAllCatalogs"},
							 // username:"root"
							}).done(function(data) {
							   console.log(data);
							   computingpolicy_db=data;
							   
							});
					   
					   
					});
				
			});
    
    }
	
  });

function order_function() {
	//alert("hello");	  
	$("#id_usergroup").empty();
	$("#id_vdc").empty();
	$("#id_cpu").empty();
	$("#id_memory").empty();
	$("#id_policy").empty();
	
	order_dialog.dialog("open");
}
  
$(document).ready(function(){  
		//loda service list;
		$.ajax({
			  crossDomain: true,
			  type:"GET",
			  //headers: { 'Access-Control-Allow-Origin': '*' },
			  url: "http://10.63.51.22:8080/ucsdportal/servicelist"
			  //dataType:"json",
			  //data: {"url":"http://10.63.51.133/app/api/rest?ormatType=json&opName=userAPIGetAllCatalogs"},
			 // username:"root"
			}).done(function(data) {
			   servicelist_handler(data);
								
			});
			
		function servicelist_handler(catalogs) {
			var datalist=catalogs.serviceResult.rows;
			console.log(datalist.length);
			var i;
			for(i=0;i<datalist.length;i++) {
				
					var temp=" <tr class=\"odd gradeX\"><td>" + datalist[i].Catalog_Name + "</td>";
				        temp+="<td>" +  datalist[i].Catalog_Description +  "</td>";
				        temp+="<td>" +  datalist[i].Cloud + "</td>";
				        temp+="<td class=\"center\">" +  datalist[i].Group + "</td>";
				        temp+="<td class=\"center\">" +  datalist[i].Status + "</td>";
				        temp+="<td class=\"center\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"order_function()\">订购</button></td></tr>";
				    //console.log("catalog_type:" + datalist[i].Catalog_Type );
				    if(datalist[i].Catalog_Type == "Standard") {
				        $("#standdata").append(temp);
				    }
				    if(datalist[i].Catalog_Type == "Advanced") {
				        $("#advandata").append(temp);
				    }
				    if(datalist[i].Catalog_Type == "Service Container") {
				        $("#containderdata").append(temp);
				    }
			}
			
		}
	
		$('#id_usergroup').change(function () {
		     var optionSelected = $(this).find("option:selected");
		     var valueSelected  = optionSelected.val();
		     var textSelected   = optionSelected.text();
		    // var optionSelected = $(this);
		     //var valueSelected  = optionSelected.val();
		     //var textSelected   = optionSelected.text();
		     var i;
		     $("#id_vdc").empty();
		     for(i=0;i<vdc_db.length;i++) {
		    	 if(textSelected == vdc_db[i].Customer_Organizations) {
		    		 $("#id_vdc").append('<option val="' + vdc_db[i].vDC + '">' + vdc_db[i].vDC + '</option>');
		    	 }
		     }
		 });
		
		$('#id_vdc').change(function () {
		     var optionSelected = $(this).find("option:selected");
		     //var valueSelected  = optionSelected.val();
		     var textSelected   = optionSelected.text();
		    // var optionSelected = $(this);
		     //var valueSelected  = optionSelected.val();
		     //var textSelected   = optionSelected.text();
		     var i;
		     var policy_name;
		     $("#id_policy").empty();
		     for(i=0;i<policy_db.length;i++) {
		    	 if(policy_db[i].vDCs.indexOf(textSelected) >=0 ) {
		    		 $("#id_policy").append('<option val="' + policy_db[i].Policy_ID + '">' + policy_db[i].Policy_Name + '</option>');
		    		 policy_name =  policy_db[i].Policy_Name;
		    	 }
		     }
		     
		     $("#id_cpu").empty();
		     $("#id_memory").empty();
		     $(computingpolicy_db).find('vmComputingPolicy').each(function(i){
		    	 var policyname=$(this).children('policyName').text();
		    	 var cpus=$(this).children('resizeAllowedCPUs').text();
		    	 var memorys=$(this).children('resizeAllowedMemoryMB').text();
		    	 //console.log("policyname:" + policyname + "policy:" +textSelected);
		    	 
		    	 if(policyname == policy_name ) {
		    		// console.log("cpus" + cpus);
		    		 var cpus_array = [];
		    		 cpus_array = cpus.split(',');
		    		 var j;
		    		 for(j=0;j<cpus_array.length;j++) {
		    			 $("#id_cpu").append('<option val="' + cpus_array[j] + '">' + cpus_array[j] + '</option>');
		    		 }
		    		 
		    		 var memory_array = [];
		    		 memory_array = memorys.split(',');
		    		 for(j=0;j<memory_array.length;j++) {
		    			 $("#id_memory").append('<option val="' + memory_array[j] + '">' + memory_array[j] + 'MB' + '</option>');
		    		 }
		    		
		    		 
		    		 
		    	 }
		    	 
		    	 //
		    	 
		    	 
		     });
		      
		    
		     
		 });
	
});
