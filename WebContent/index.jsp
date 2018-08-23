<%@ page pageEncoding="UTF-8" contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> 
<html> 
    <head> 
        <title>Memories</title>         
        <meta charset="utf-8" /> 
        <link rel="stylesheet" href="css/bootstrap.min.css" /> 
		<link rel="stylesheet" href="css/pageStruct.css" /> 
 		<link rel="stylesheet" href="css/ins.css" />
    </head>
    <body> 
         <script type="text/javascript">
      	 function redTo(key,page)
     	 {
     		var f = document.createElement("form");
     		f.action = page;
     		f.method = "POST";
     		var i = document.createElement("input");
     		i.type = "hidden";
     		i.name = "key";
     		i.value = key;
     		f.appendChild(i);
     		document.body.appendChild(f);
     		f.submit();
     	 }
         
      	 <c:choose>
          <c:when test="${session_expired}">
          	localStorage.clear();
          </c:when>
          <c:when test="${goToProfile}">
          	if(localStorage.userKey != undefined)
          	{
          		redTo(localStorage.userKey,"profile");	
          	}
          </c:when>
          <c:otherwise>
          	if(localStorage.userKey != undefined)
          	{
          		redTo(localStorage.userKey,"home");	
          	}
          </c:otherwise>
         </c:choose>
         </script>
        <header> 
            <nav class="navbar navbar-inverse"> 
                <div class="container-fluid"> 
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#login">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span> 
                    </button>
                    <div class="navbar-header">
                        <a class="navbar-brand" href="#">Memories</a>
                    </div>                     
                    <ul class="nav navbar-nav navbar-right collapse navbar-collapse" id="login"> 
                        <li>
                            <a data-toggle="modal" data-target="#form-connexion" href="#"><span class="glyphicon glyphicon-log-in hidden-xs "></span> Connexion</a>
                        </li>                         
                    </ul>                     
                </div>                 
            </nav>             
        </header>  
        <div class="alert page-alert " id="alert">
            <button type="button" class="close">
                <span aria-hidden="true">×</span>
                <span class="sr-only">Close</span>
            </button>
            <p id="alert-text" class="text-center"></p>
        </div>
        <c:if test="${session_expired}">
         <div class="alert alert-warning" id="alert_session">
            <button type="button" class="close" onclick="$(this).closest('#alert_session').slideUp();">
                <span aria-hidden="true">×</span>
                <span class="sr-only">Close</span>
            </button>
            <p class="text-center">Votre Session a expirer</p>
         </div>   
        </c:if>  
        <section id="page" class="container alerts">
            <section id="content" style="display:flex;"> 
                <div class="ins-image"> 
                    <img src="img/network.png" /> 
                </div>                 
                <div class="ins-form well"> 
                    <form id="ins-form"> 
                        <h3>Noveau sur Memories ? Inscrivez-vous</h3>                         
                        <div class="form-group"> 
                            <input type="text" class="form-control" id="nom" name="nom" placeholder="Nom" required /> 
                        </div>                         
                        <div class="form-group"> 
                            <input type="text" class="form-control" id="prenom" name="prenom" placeholder="Prenom" required /> 
                        </div>                         
                        <div class="form-group"> 
                            <input type="text" class="form-control" id="username" name="username" placeholder="Nom d'utilisateur" required /> 
                        </div>                                                
                        <div class="form-group"> 
                            <input type="password" class="form-control" id="password" placeholder="Mot de passe" required name="password" /> 
                        </div>
                        <div class="form-group"> 
                            <input type="password" class="form-control" id="password_confirm" placeholder="Confirmation du mot de passe" required /> 
                        </div>                            
                        <div class="form-group"> 
                            <input type="submit" value="Inscription" id="ins-button" class="btn btn-default form-control"  disabled/> 
                        </div>                         
                    </form>                     
                </div>                            
        </section>         
        <div class="modal fade" id="form-connexion"> 
            <div class="modal-dialog"> 
                <div class="modal-content"> 
                    <div class="modal-header"> 
                        <a href="#" class="btn btn-default pull-right" data-dismiss="modal">x</a> 
                        <h4 class="modal-title">Connexion :</h4> 
                    </div>                     
                    <div class="modal-body"> 
                    	<p class="alert-danger text-center" id="login-error"></p>
                        <form action="#" method="post"> 
                            <div class="form-group row"> 
                                <div class="col-sm-8 col-sm-offset-2"> 
                                    <input type="text" class="form-control col-sm-8" name="username" id="login_username" placeholder="Nom d'utilisateur" /> 
                                </div>                                 
                            </div>                             
                            <div class="form-group row"> 
                                <div class="col-sm-8 col-sm-offset-2"> 
                                    <input type="password" class="form-control" name="password" id="login_password" placeholder="Mot de passe" /> 
                                </div>                                 
                            </div>                         
                            <div class="row"> 
                                <div class="col-sm-8 col-sm-offset-2"> 
                                    <button type="submit" id="connect_button" class="btn btn-primary col-sm-12">Connexion</button>                                                                      
                                </div>                                 
                            </div>                             
                        </form>                         
                    </div>                     
                </div>                 
            </div>             
        </div>         
    </section>     
    <footer class="text-center"> 
        <p>© 2016 Memories</p>
    </footer>    
    <script type="text/javascript" src="js/jquery.min.js"></script>     
    <script type="text/javascript" src="js/bootstrap.min.js"></script> 
    <script type="text/javascript" src="js/alert.js"></script>   
    <script type="text/javascript">
    
    ajax_ok = "{}";
    
    
    function typeOfError(code)
    {
    	switch(code)
    	{
   		 	case 0 : return "données incorrect";
    		case 1 : return "données existante";
    		case 2 : return "données manquantes";
    		case 3 : return "session expirer";
    		case 1000 : return "erreur de base de donnée";
   		 	case 10000 : return "erreur code serveur";
   		 	default: return "Erreur";
    	}
    }
    /* modal for connexion */
	  	var modalVerticalCenterClass = ".modal";
	  	function centerModals($element) {
	    var $modals;
	    if ($element.length) {
	        $modals = $element;
	    } else {
	        $modals = $(modalVerticalCenterClass + ':visible');
	    }
	    $modals.each( function(i) {
	        var $clone = $(this).clone().css('display', 'block').appendTo('body');
	        var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 3);
	        top = top > 0 ? top : 0;
	        $clone.remove();
	        $(this).find('.modal-content').css("margin-top", top);
	    });
	   }
	   $(modalVerticalCenterClass).on('show.bs.modal', function(e) {
	      centerModals($(this));
	   });
	   $(window).on('resize', centerModals);
	   
	   
	   /* Events */
	   $(document).ready(function() {
	   
		 $("#connect_button").click(function(e)
	   	 {
	   	 	var username = $("#login_username").val();
	   	 	var password = $("#login_password").val();
	   	 	var data_to_send = {"username":username,"password":password};

	   		$.ajax({
	   			type: 'POST',
	   			url: 'user/login',
	   			data: data_to_send,
	   			success: function(response) {
	   				if(response.message == undefined) // pas de message d'erreur
	   				{
	   					localStorage.userKey = response.key;
	   					localStorage.userId = response.id;
	   					localStorage.userLogin = response.username;
	   					redTo(response.key,"home");
	   				}
	   				else
	   				{
	   					$("#login-error").html(response.message);
	   				}

	   			},
	   			error : function() {
	   				$("#login-error").html("Memories n'arrivent pas a vous connecter");
	   			}
	   		});
	   		e.preventDefault();
	   	 });
	   	 
		 // click sur le button d'inscription
		 
		 $("#ins-button").click(function(e) {
	   		 
	   		 var nom = $("#nom").val();
	   		 var prenom = $("#prenom").val();
	   		 var username = $("#username").val();
	   		 var password = $("#password").val();
	   		 // data to send to the server 
	   		 var data_to_send = {"nom":nom ,"prenom":prenom,"username":username,"password":password};
	   		 // ajax
	   		 $.ajax({
	   			 type: 'POST',
	   			 url: 'user/create',
	   			 data : data_to_send,
	   			 success : function(response)
	   			 { 
	   				 if(response.message != undefined)
	   				 {
	   				 	var message = response.message.replace(/\"([\s\w\']+)\"/g,"<b>$1</b>")
	   				 	$("#alert-text").html("<strong>erreur de type:</strong> "+typeOfError(response.code)+" ,"+message);
	   				 	$("#alert").addClass("alert-danger").removeClass("alert-success");
	   				 	$("#alert").trigger("alertEvent");

	   				 }
	   				 else {
	   					$("#alert-text").html("<strong>Inscription terminer</strong>");
	   					$("#alert").addClass("alert-success").removeClass("alert-danger");
	   					$("#alert").trigger("alertEvent");
	   				 }
	   			 },
	   			 error : function()
	   			 {
	   			 	$("#alert-text").html("<strong>Impossible de joindre le serveur</strong>");
	   				$("#alert").addClass("alert-danger").removeClass("alert-success");
	   				$("#alert").trigger("alertEvent");
	   			 }
	   		 });
	   		 e.preventDefault();
	   	 });
   		});
	   
	   // verification du formulaire d'inscription
	   	    
	   var name_regex = /^[a-zA-Z]+$/;
	   var regex = /^[a-zA-Z0-9]$/;
	   var username_regex = /^[a-zA-Z][a-zA-Z0-9]+$/;
	   var pass_regex = /^.{8,}$/;
	   
	   flag_nom = 0; flag_prenom = 0;flag_username = 0; flag_password = 0; flag_password_confirm = 0;
	
	   function formChangeEvent()
	   {
	      global_flag =  flag_nom * flag_prenom * flag_password * flag_username * flag_password_confirm;
	      if(global_flag == 1)
	      {
	        $("#ins-button").removeAttr("disabled").removeClass("btn-default").addClass("btn-primary");
	      }
	      else {
	        $("#ins-button").attr("disabled","").removeClass("btn-primary").addClass("btn-default");
	      }
	   }
	   
	   $(document).ready(  
	      function() {
	        $("#nom").change(function() {
	          if(name_regex.test($(this).val()))
	          {
	              $("#nom").parent().addClass("has-success").removeClass("has-error");
	              flag_nom = 1;
	          }
	          else
	          {
	              $("#nom").parent().addClass("has-error").removeClass("has-success");
	              flag_nom = 0;
	          }
	          formChangeEvent();
	        });
	
	         $("#prenom").change(function() {
	          if(name_regex.test($(this).val()))
	          {
	              $("#prenom").parent().addClass("has-success").removeClass("has-error");
	              flag_prenom = 1;
	          }
	          else
	          {
	              $("#prenom").parent().addClass("has-error").removeClass("has-success");
	              flag_prenom = 0;
	          }
	          formChangeEvent();
	        });
	
	         $("#username").change(function() {
	          if(username_regex.test($(this).val()))
	          {
	              $("#username").parent().addClass("has-success").removeClass("has-error");
	              flag_username = 1;
	          }
	          else
	          {
	              $("#username").parent().addClass("has-error").removeClass("has-success");
	              flag_username = 0;
	          }
	          formChangeEvent();
	        });
	
	         $("#password").change(function() {
	          if(pass_regex.test($(this).val()))
	          {
	              $("#password").parent().addClass("has-success").removeClass("has-error");
	              flag_password = 1;
	          }
	          else
	          {
	              $("#password").parent().addClass("has-error").removeClass("has-success");
	              flag_password = 0;
	          }
	          formChangeEvent();
	        });
	         
	         $("#password_confirm").change(function() {
		          if($(this).val() == $("#password").val())
		          {
		              $("#password_confirm").parent().addClass("has-success").removeClass("has-error");
		              flag_password_confirm = 1;
		          }
		          else
		          {
		              $("#password_confirm").parent().addClass("has-error").removeClass("has-success");
		              flag_password_confirm = 0;
		          }
		          formChangeEvent();
		        });
	      }
       );
  </script>     
</body>
