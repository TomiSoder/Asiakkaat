<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<script src="scripts/main.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta charset="ISO-8859-1">
<title>Asiakkaan muutos</title>
</head>
<body>
	<form id="tiedot">
		<table>
			<thead>
				<tr>
					<th colspan="5" class="oikealle"><span id="takaisin">Takaisin asiakaslistaan</span></th>
				</tr>
				<tr>
					<th>Etunimi</th>
					<th>Sukunimi</th>
					<th>Puhelin</th>
					<th>Sähköposti</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" name="etunimi" id="etunimi"></td>
					<td><input type="text" name="sukunimi" id="sukunimi"></td>
					<td><input type="text" name="puhelin" id="puhelin"></td>
					<td><input type="text" name="sposti" id="sposti"></td>
					<td><input type="submit" id="tallenna" value="Hyväksy"></td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" name="asiakas_id" id="asiakas_id">
	</form>
	<span id="ilmo"></span>
</body>
<script>
$(document).ready(function(){
	$("#takaisin").click(function() {
		document.location = "listaaasiakkaat.jsp";
	});
	var asiakas_id = requestURLParam("asiakas_id");
	$.ajax({url:"asiakkaat/haeyksi/"+asiakas_id, type:"GET", dataType:"json", success:function(result){
		$("#asiakas_id").val(result.asiakas_id);
		$("#etunimi").val(result.etunimi);
		$("#sukunimi").val(result.sukunimi);
		$("#puhelin").val(result.puhelin);
		$("#sposti").val(result.sposti);
	}});
	
	$("#tiedot").validate({
		rules : {
			etunimi : {
				required : true,
				minlength : 2
			},
			sukunimi : {
				required : true,
				minlength : 2
			},
			puhelin : {
				required : true,
				minlength : 5,
				maxlength : 15
			},
			sposti : {
				required : true,
				email : true
			}
		},
		messages : {
			etunimi : {
				required : "Arvo puuttuu",
				minlength : "Liian lyhyt"
			},
			sukunimi : {
				required : "Arvo puuttuu",
				minlength : "Liian lyhyt"
			},
			puhelin : {
				required : "Arvo puuttuu",
				minlength : "Liian lyhyt",
				maxlength : "Liian pitkä"
			},
			sposti : {
				required : "Arvo puuttuu",
				email : "Ei ole kelvollinen sähköposti"
			}
		},
		submitHandler : function(form) {
			paivitaTiedot();
		}
	});
});
//Tietojen päivitys, kutsutaan backing PUT-metodia
//PUT /asiakkaat/
function paivitaTiedot(){	
	var formJsonStr = formDataJsonStr($("#tiedot").serializeArray()); //muutetaan lomakkeen tiedot json-stringiksi
	$.ajax({url:"asiakkaat", data:formJsonStr, type:"PUT", dataType:"json", success:function(result) { //result on joko {"response:1"} tai {"response:0"}       
		if(result.response==0){
      	$("#ilmo").html("Asiakkaan päivittäminen epäonnistui.");
      }else if(result.response==1){			
      	$("#ilmo").html("Asiakkaan päivittäminen onnistui.");
      	$("#etunimi", "#sukunimi", "#puhelin", "#sposti").val("");
	  }
  }});	
}
</script>
</html>