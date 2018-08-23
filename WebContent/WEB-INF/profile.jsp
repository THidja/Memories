<%@ page pageEncoding="UTF-8" contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Memories</title>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/pageStruct.css" />
        <style type="text/css">
        	.panel-heading {
        		 margin-top:10px;
        		 border-radius:10px;
        		 text-align:center;
        	}
        </style>
    </head>
    <body>
        <header>
            <nav class="navbar navbar-inverse">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span> 
                        </button>
                        <a class="navbar-brand" href="index">Memories</a>
                    </div>
                    <div class="collapse navbar-collapse" id="myNavbar">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a class="dropdown-toggle connectedUser" data-toggle="dropdown" href="#"><span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="index"><span class="glyphicon glyphicon-home"></span> Accueil</a>
                                    </li>
                                    <li>
                                        <a href="#" id="logout"><span class="glyphicon glyphicon-remove"></span> Deconnexion</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <section id="page" class="container">
            <section id="content alerts" class="row">
               <div class="alert page-alert row" id="alert" data-delay="5000" disabled>
                 <button type="button" class="close">
                   <span aria-hidden="true">×</span>
                   <span class="sr-only">Close</span>
           		 </button>
           		 <p id="alert-text" class="text-center"></p>
       		   </div>
       		   <div class="col-md-6">
       		   	  <div class="thumbnail col-md-12">
                    <img alt="userPicture" class="img-rounded userPicture" width="250" height="250" />
                    <div class="caption text-center">
                        <h3 class="connectedUser"></h3> 
                    </div>
       		      </div>
       		      
       		         			   
   			   <div class="panel panel-default col-md-12">
            	 <div class="panel-heading">
        		  	 <h4>Modifier ma photo de profile</h4>
     			 </div>
      			 <div class="panel-body content-body">
        	   	    <form class="form" method="post" action="#">
        	   	 	    <div class="form-group"> 
                            <label class="control-label" for="inputFile">Photo de profile :&nbsp;</label>                             
                            <input type="file" id="inputFile">
                        </div>
                        <br />
                        <div class="form-group pull-right">
                            <input type="button" data-toggle="page-alert" class="btn btn-primary form-control" id="changePicBtn" value="Appliquer" />
                        </div>
        			</form>
   				</div>
   			   </div>
       		   </div>
               <div class="col-md-6">
	               <div class="panel panel-default col-md-12">
	   			     <div class="panel-heading">
	                  <h4>Modifier mes informations personnelles</h4>
	                 </div>
	                 <div class="panel-body content-body">
	                   <form class="form" method="post" action="#">
	                        <div class="form-group">
	                            <label for="nom">Nom:</label>
	                            <input type="text" name="nom" id="nom" class="form-control" />
	                        </div>
	                        <div class="form-group">
	                            <label for="prenom">Prenom:</label>
	                            <input type="text" name="prenom" id="prenom" class="form-control" />
	                        </div>
	                        <div class="form-group pull-right">
	                            <input type="button" data-toggle="page-alert" class="btn btn-primary form-control" id="changeInfoBtn" value="Appliquer" />
	                        </div>
	                   </form>
	                 </div>
	   			   </div>
	   			   
		   		   <div class="panel panel-default col-md-12">
				   	  <div class="panel-heading">
				        <h4>Modifier mon mot de passe</h4>
				      </div>
				      <div class="panel-body content-body">
				        <form class="form" method="post" action="#">
				          <div class="form-group">
				            <input type="password" class="form-control" id="prevPass" placeholder="Ancien mot de passe"></input>
				          </div>
				          <div class="form-group">
				            <input type="password" class="form-control" id="newPass" placeholder="Noveau mot de passe"></input>
				          </div>
				          <div class="form-group">
				            <input type="password" class="form-control" id="newPassConfirm" placeholder="Confirmation du noveau mot de passe"></input>
				          </div>
				          <div class="form-group pull-right">
				            <input type="button" class="btn btn-primary form-control" id="changePassBtn" value="Appliquer" class="pull-right"></input>
				          </div>
				        </form>
				      </div>
	   			   </div>
               </div>
            </section>
        </section>
        <footer class="text-center">
            <p>&copy; 2016 Memories</p>
        </footer>
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/alert.js"></script>
        <script type="text/javascript">
        	function showAnError(message)
        	{
				$("#alert-text").html(message);
        		$("#alert").addClass("alert-danger").removeClass("alert-success");
				$("#alert").trigger("alertEvent");
        	}
        	
        	function showSuccess(message)
        	{
				$("#alert-text").html(message);
        		$("#alert").addClass("alert-success").removeClass("alert-danger");
				$("#alert").trigger("alertEvent");
        	}
        	
        	
        	$(document).ready(function() {
        		
                $("#logout").click(function(e) {
                    
                    // données a envoyer au serveur 
                	var data = {'key':localStorage.userKey};
                
                	$.ajax({
                		type: 'POST',
                		url: 'user/logout',
                		data: data,
                		success: function(response) {

                			if(JSON.stringify(response) == "{}")       
                            {     			
                				localStorage.clear();
                				window.location.href = 'index';
                			}
                			else
                			{
                				showAnError(response.message);
                			}
                		},
                		error: function(xhr, ajaxOptions, thrownError) {
                				showAnError("Memories n'arrivent pas a vous deconnecter");
                		}
                	});
                
                	e.preventDefault();
                });
                
        		$(".connectedUser").prepend(localStorage.userLogin + " ").css("text-transform","capitalize");
        		$(".userPicture").attr("src","picture/get?username="+localStorage.userLogin);
        		$("#changeInfoBtn").on("click",function(event) {
        			var nom = $("#nom").val();
        			var prenom = $("#prenom").val();
        			$.ajax({
        				type: 'POST',
        				url: 'user/updateInfo',
        				data: {'key':localStorage.userKey,'nom':nom,'prenom':prenom},
        				success: function(response)
        				{
        					if(response.message == undefined)
        					{
        						showSuccess("informations mises a jour");
        					}
        					else
        					{
        						showAnError(response.message);	
        					}
        				},
        				error: function (response) {
        					showAnError("Memories n'arrivent pas a mettre a jour vous informations");
        				}
        			});
        		});
        		$("#changePicBtn").click(function() {
        			var fileInput = $("#inputFile").get(0);
        			var formData = new FormData();
        			formData.append("picture",fileInput.files[0]);
        			formData.append("key",localStorage.userKey);
        			$.ajax({
        				type: 'POST',
        				url: 'picture/set',
        				cache:false,
        				processData: false,
        				contentType:false,
        				data: formData,
        				success: function(response) {
        					if(response.message != undefined)
        					{
        						showAnError(response.message);
        					}
        					else
        				    {
        						location.reload();
        				    }
        				},
        				error: function() {
        					showAnError("Memories n'arrivent pas a mettre a jour votre photo de profile");
        				}
        			});
        		});
        		
        		$("#changePassBtn").click(function() {
        				var prevPass = $("#prevPass").val();
        				var newPass = $("#newPass").val();
        				var newPassConfirm = $("#newPassConfirm").val();
        				if(newPass != newPassConfirm)
        				{
        					showAnError("les mots de passe ne sont pas identiques");	
        				}
        				else
        				{
        					$.ajax({
        						type:'POST',
        						url: 'user/updatePassword',
        						data : {'prevPass':prevPass,'newPass':newPass,'key':localStorage.userKey},
        						success: function(response) {
        							if(response.message != undefined)
        							{
        								showAnError(response.message);	
        							}
        							else
        							{
        								showSuccess("Votre mot de passe a etait mis a jour");
        							}},
        						error: function()
        						{
        							console.log("erreur ajax on updatePassword");
        						}
        						
        					});
        				}
        		});
        	});
        </script>
    </body>
</html>
