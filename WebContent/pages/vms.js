/**
 * 
 */

var dialog_vmdetail;

dialog_vmdetail = $( "#dialog-vmdetail" ).dialog({
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
    }
  });

function vmdetail_view(serviceid) {
	
	console.log(serviceid);
	dialog_vmdetail.dialog("open");
}
  
$(document).ready(function(){  
		//loda service list;
		$.ajax({
			  crossDomain: true,
			  type:"GET",
			  //headers: { 'Access-Control-Allow-Origin': '*' },
			  url: "http://10.63.51.22:8080/ucsdportal/vmlist"
			  //dataType:"json",
			  //data: {"server":"http://10.63.51.133","url":"/app/api/rest?formatType=json&opName=userAPIGetServiceRequests&opData={}"}
			 // username:"root"
			}).done(function(data) {
			   vmlist_handler(data);
								
			});
			
		function vmlist_handler(vms) {
			var datalist=vms.serviceResult.rows;
			console.log(datalist.length);
			var i,j;
			j=0;
			for(i=0;i<datalist.length;i++) {
				
					
					var temp=" <tr class=\"odd gradeX\"><td>" + datalist[i].Service_Request_Id + "</td>";
				        temp+="<td>" +  datalist[i].Initiating_User +  "</td>";
				        temp+="<td>" +  datalist[i].Catalog_Workflow_Name + "</td>";
				        temp+="<td class=\"center\">" +  datalist[i].Request_Time + "</td>";
				        temp+="<td class=\"center\">" +  datalist[i].Request_Status + "</td>";
				        temp+="<td class=\"center\"><button type=\"button\" class=\"btn btn-primary\" onclick=\"vmdetail_view(" + datalist[i].Service_Request_Id +")\">详情</button></td></tr>";
				    //console.log("catalog_type:" + datalist[i].Catalog_Type );
				    if(datalist[i].Request_Type == "Create VM") {
				    	j++;
				        $("#vmlist").append(temp);
				    }
				    if(j==11) break;
				    
				    
			}
			
		}
	
		
});
