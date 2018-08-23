<%@ page pageEncoding="UTF-8" contentType="text/html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Memories</title>
        <meta charset="utf-8" />
 		 <link rel="stylesheet" href="css/bootstrap.min.css" /> 
 		 <link rel="stylesheet" href="css/pageStruct.css" /> 
 		 <link rel="stylesheet" href="css/home.css" /> 
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
                        <a class="navbar-brand" href="#">Memories</a>
                    </div>
                    <div class="collapse navbar-collapse" id="myNavbar">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a class="dropdown-toggle" data-toggle="dropdown" id="connected_user" href="#"><span class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a href="profile"><span class="glyphicon glyphicon-user"></span> mon profile</a>
                                    </li>
                                    <li>
                                        <a href="#" id="logout"><span class="glyphicon glyphicon-remove"></span> Deconnexion</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <span class="glyphicon glyphicon-search hidden-xs"></span>
                                    <span class="visible-xs">Rechercher</span>
                                </a>
                                <ul class="dropdown-menu">
                                    <form class="form container-fluid">
                                        <div class="form-group">
                                            <input type="search" class="form-control" placeholder="Mot-cle" id="searchArea">
                                        </div>
                                        <div class="form-group">
                                        	<label>
                                           	  <input type="checkbox" id="onlyfriends" /> Filtrer
                                            </label>
                                        </div>
                                    </form>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <div class="alert page-alert alert-danger" id="alert" data-delay="5000" disabled>
            <button type="button" class="close">
                <span aria-hidden="true">×</span>
                <span class="sr-only">Close</span>
            </button>
            <p id="alert-text" class="text-center"></p>
        </div>
        <div class="container" id="page">
            <div class="row" id="content">
                <div class="col-sm-4 col-sm-offset-1">
                    <div class="row add-comment">
                        <h3 class="header">Ajouter un commentaire</h3>
                        <textarea class="form-control lead" rows="4" id="add-comment-text" placeholder="Votre commentaire"></textarea>
                        <button class="btn btn-primary pull-right" type="submit" id="add-comment-button">Ajouter</button>                         
                    </div>  
                    <div class="row">
                        <h3 class="header">Notifications</h3>
                        <div id="notifications" >
                        
                        </div>   
                    </div>                
                </div>
                <div class="col-sm-6 col-sm-offset-1">
                 <div class="row">
                   <h3 class="header">Liste des commentaires</h3>
                   <div  id="comments">
                    
                   </div>   
                 </div>
                </div>              
            </div>
        </div>
    <footer class="text-center">
        <p>© 2016 Memories</p>
    </footer>
    <script type="text/javascript" src="js/jquery.min.js"></script>     
 	<script type="text/javascript" src="js/bootstrap.min.js"></script> 
    <script type="text/javascript" src="js/alert.js"></script>      
    <script type="text/javascript">	

        // global data : informations about the connected user

        window.userId = localStorage.userId;
        window.userLogin = localStorage.userLogin;
        window.userKey = localStorage.userKey;

        // afficher dans la bare de navigation le login de l'utilisateur connecter
        
        $("#connected_user").prepend(userLogin + ' ').css("text-transform","capitalize");

        // Session Manager

        // a chaque envoie d'une requette http de type xhr (ajax) , la cle est ajouter automatiquement au données envoyer
        $.ajaxPrefilter(function( options, originalOptions, jqXHR ) {
            options.data = $.param($.extend(originalOptions.data,{'key':userKey}));
        });
		// a la reception d'une reponse suite a une requette ajax , on verifer la session
        $(document).ajaxComplete(function(event,xhr,options)
        {
            var json = JSON.parse(xhr.responseText);
            if(json.code != undefined && json.code == 3) // code == 3 - Session expired
            {
                window.location.href = "index";
            }
        });


        // reponse retourner par le serveur dans le cas ou la requette ajax est executer sans erreur sur le serveur

        window.ajax_ok = '{}';

        // a ameliorer 

        function showAnError(content)
        {
            $("#alert-text").html("<strong>"+' '+"</strong> "+content);
            $("#alert").trigger("alertEvent");
        }


        $("#logout").click(function(e) {
        
            // données a envoyer au serveur 
        	var data = {};
        
        	$.ajax({
        		type: 'POST',
        		url: 'user/logout',
        		data: data,
        		success: function(response) {

        			if(JSON.stringify(response) == ajax_ok)       
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



    // un objet qui regroupe les commentaires de la pages ainsi que leurs auteurs et les notifications de l'utilisateur connecter
    // cette objet est utliser pour faciliter la mis a jour de la page

	environement = { users: [] , comments: []};
	
    // le modele de l'application

    Date.prototype.heure = function(heure,minutes)
    {
        if(minutes < 10)
          minutes = "0" + minutes;
        return heure + ":" + minutes;
    }


    Date.prototype.getWeek = function(){
        var d = new Date(+this);
        d.setHours(0,0,0);
        d.setDate(d.getDate()+4-(d.getDay()||7));
        return Math.ceil((((d-new Date(d.getFullYear(),0,1))/8.64e7)+1)/7);
    };

    Date.prototype.toFrString = function() {
        
        var jours = ["Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"];
        var mois = ["janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre", "octobre", "novembre", "decembre"];

        var out = "";
        var d = new Date();
        
        if(d.getFullYear() == this.getFullYear())
        {
            if(d.getMonth() == this.getMonth())
            {
                if(d.getDay() == this.getDay()) // ex : Aujourd'hui, à 02:00
                {
                    out =  "Aujourd'hui, à "+this.heure(this.getHours(),this.getMinutes());        
                }
                else if(d.getDay() == this.getDay() + 1) // ex:   Hier, à 02:00
                {
                    out =  "Hier, à "+this.heure(this.getHours(),this.getMinutes()); 
                }
                else // ex: Samedi 16, 02:00
                {
                    out += jours[this.getDay()];
                    out += " " + this.getDate() + ", ";
                    out += " " + this.heure(this.getHours(),this.getMinutes());
                }
            }
            else // ex: 16 Janvier, 02:00
            {
                out += this.getDate();
                out += " " +  mois[this.getMonth()]+", ";
                out += " " + this.heure(this.getHours(),this.getMinutes());
            }
        }
        else // ex: 16 Janvier 2016, 02:00
        {
            out += this.getDate();
            out += " "+ mois[this.getMonth()];
            out += " "+ this.getFullYear()+", ";
            out += " "+ this.heure(this.getHours(),this.getMinutes()); 
        }

        return out;
    }

    // l'identifiant de l'utilisateur est obligatoire a la construction pour savoir a quelle indice on dois l'ajouter dans l'environement
 	function User(id)
 	{
 		this.id = id;
 		this.login = "";
 		this.contact = 0; // si l'utilisateur connecter est ami avec cette utilisateur
 		environement.users[this.id] = this;
 	}
 	
 	function Notification(from,login,type)
 	{
 		this.from = from; // l'utlisateur qui as declencher la notification
 		this.login = login; // son login
 		this.type = type; // un attribut decrivant de quelle type de notification il s'agit (par exemple une demande d'ajout d'ami)
 	}
 	

    // le code html de la notification 

 	Notification.prototype.getHtml = function() {
 		
 		var html = '<div class="notification" id="Notif_'+this.from+this.type+'">';
		// une demande d'ajout d'ami
 		if(this.type == "request")
 		{
            html += '<img src="picture/get?username='+this.login+'" width="50" height="50" class="img-circle" />';
 			html += '<span><b>'+this.login+'</b> vous a envoyer une demande d\'ajout</span>';
 			html += '<br/>';
 			html += '<div class="button-group text-right">';
 			html += '<button class="btn btn-default accept-btn" onclick="acceptFriend('+this.from+')"><img src="img/accept.png" /></button>'; 
 			html += '<button class="btn btn-default refuse-btn" onclick="refuseFriend('+this.from+')"><img src="img/refuse.png" /></button>';
 			html += '</div>';
 		}
		// acceptation
 		else if(this.type == "accept")
 		{
            html += '<img src="picture/get?username='+this.login+'" width="50" height="50" class="img-circle" />';
 			html += '<span><b>'+this.login+'</b> a accepter votre demande d\'ajout</span>'; 				
 		}
 		html += '</div>';
 		return html;
 	}
 	
    // ajouter la code html de la notification a la page 
 	function htmlAddNotification(notif)
 	{
 		$("#notifications").prepend(notif.getHtml());
        $("#Notif_"+notif.id+notif.type).hide().fadeIn('slow');
 	}

    function htmlDeleteNotification(notifID)
    {
        $("#Notif_"+notifID).fadeOut('slow',function() {
            $("#Notif_"+notifID).remove();
        });
    }
    
    // l'identifiant du commentaire est obligatoire pour la meme raison
    function Commentaire(comment_id) {
     		this.id = comment_id; // l'identifiant du commentaire
     		this.auteur = undefined; // l'auteur du commentaire
     		this.texte = ""; // le texte du commentaire
     		this.date = new Date(); // la date de creation du commentaire 
     		this.score = undefined;
     		this.nblike = 0; // le nombre de like du commentaire
     		this.ilike = false; // si l'utilisateur connecter aime ce commentaire
     		environement.comments[comment_id] = this; 
    }

    Commentaire.prototype.getAppropiateButtons = function() {
    			
    		html = '<div class="button-group" >';
    		if(this.auteur.id != userId) // l'utilisateur connnecter n'est pas l'auteur du commentaire
    		{
    			if(this.auteur.contact == 1) // l'auteur est dans ma liste d'amis
    			{
    				html += '<button class="btn btn-default btn-rem-friend" type="button" onclick="removeFriend('+this.auteur.id+')"><img src="img/user-remove.png" /> Retirer</button>';
    			}
    			else if(this.auteur.contact == 2) // l'utilisateur connecter a envoyer une demande d'ajout
    			{
    				html += '<button class="btn btn-default friend_request" type="button"><img src="img/user-add.png" /> Demande d\'ajout envoyer</button>';
    			}
                else if(this.auteur.contact == 3) // l'auteur a envoyer une demande d'ajout a l'tilisateur connecter
                {
                    html += '<button class="btn btn-default btn-add-friend" type="button" onclick="acceptFriend('+this.auteur.id+')"><img src="img/user-add.png" /> Accepter</button>';   
                }
    			else
    			{
    					html += '<button class="btn btn-default btn-add-friend" type="button" onclick="addFriend('+this.auteur.id+')"><img src="img/user-add.png" /> Ajouter</button>';		
    			}

        		if(this.nblike == 0)
        		{
    	       		html += '<button type="button" class="btn btn-default like-btn" onclick="like(\''+this.id+'\')" ><img src="img/no-like.png" /><span class="nblike"> 0</span></button>';
    		    }
    	       	else
    		    {
    			     if(this.ilike)
				     {
    				    html += '<button type="button" class="btn btn-default ilike like-btn" onclick="unlike(\''+this.id+'\')"><img src="img/like.png" /><span class="nblike"> '+this.nblike+'</span></button>';
    			     }
				    else
				    {
    				    html += '<button type="button" class="btn btn-default like-btn" onclick="like(\''+this.id+'\')"><img src="img/like.png" /><span class="nblike"> '+this.nblike+'</span></button>';
    			     }
    	       	}
            }
    	    else 
    	    {
    	      	if(this.nblike == 0)
    	      	{
    	      		html += '<button type="button" class="btn btn-default like-btn"><img src="img/no-like.png" /><span class="nblike"> 0</span></button>';
    	      	}
    	      	else
    	      	{
    	      		html += '<button type="button" class="btn btn-default like-btn"><img src="img/like.png" /><span class="nblike"> '+this.nblike+'</span></button>'; 
    	      	}
    	      	html += '<button type="button" class="btn btn-default del-button pull-right" onclick="deleteComment(\''+this.id+'\')"><span class="glyphicon glyphicon-remove"></span></button>';
    	    }
    	      html += '</div>';
    	      return html;
         };

     	Commentaire.prototype.getHtml = function() {
     		
     		var html = '<div class="comment row" id="Comment_'+this.id+'">'
           				 +'<div class="text-center col-md-2">'
            				+'<img src="picture/get?username='+this.auteur.login+'" alt="" width="80" height="80" class="img-circle">' 
        				 +'</div>'
       					 +'<div class="col-md-10 comment-content">'
            				+'<span class="comment-content-title">'+this.auteur.login+'</span>' 
            				+'<span class="date pull-right"> '+this.date.toFrString()+'</span>'
            				+'<p class="comment-content-text">'+this.texte+'</p>' 
            				+ this.getAppropiateButtons();
            		     +'</div>'
            		  +'</div>';
            return html; 
     	}

     	function htmlAddComment(comment)
     	{
     		$("#comments").prepend(comment.getHtml());	
            $("#Comment_"+comment.id).hide().fadeIn('slow');
            if(comment.auteur.id == userId)
            {
            	var htmlCode = '<ul class="dropdown-menu">'
            				  +'<li><a href="#" onclick="deleteComment('+comment.id+')">Supprimer</a></li>'
            				  +'</ul>';
            	$("#Comment_"+comment.id).prepend(htmlCode);
            	$("#Comment_"+comment.id).attr("data-toggle","dropdown");
            			
            }
            
            
     	}
        function htmlDeleteComment(comment_id)
        {
            $("#Comment_"+comment_id).fadeOut('slow',function() {
                $("#Comment_"+comment_id).remove();
            })
        }     	
        
        function deleteComment(comment_id)
        {
        	var data = {'comment_id':comment_id};
        	$.ajax({
        		type: 'POST',
        		url: 'comment/delete',
        		data: data,
        		success: function(response) {
        			if(response.message != undefined)
        			{
        				showAnError(response.message);	
        			}
        			else
        			{
        				delete environement.comments[comment_id];
        				htmlDeleteComment(comment_id);
        			}
        		},
        		error: function() {
        		}
        	});
        }
        
     	$("#add-comment-button").on("click",function(e) {
     		if($("#add-comment-text").val().length < 3)
     		{
     			showAnError("un commentaire ne peux pas avoir moins de 3 caracteres");
     		}
     		else
     		{
     		   var comment_text = $("#add-comment-text").val();
               var data = {'text':comment_text};

     		   $.ajax({
     			   type: 'POST',
     			   url: 'comment/add',
     			   data: data,
     			   success: function(response) {
     				   if(response.message != undefined)
     				   {
     						showAnError(response.message);  
     				   }
     				   else
     				   {
     					   var comment = new Commentaire(response.id);
     					   comment.auteur = new User(userId);
     					   comment.auteur.login = userLogin;
     					   comment.texte = comment_text;
                           comment.date = new Date(parseInt(response.at));
     					   $("#add-comment-text").val("");
     					   htmlAddComment(comment);
     				   }
     			   },
     			   error: function() {
     		             showAnError("Memories n'arrivent pas a ajouter votre commentaire");
     			   }
     		   })
     		}
     	});

        function updateCommentButtons(comment_id)
        {
             $("#Comment_"+comment_id+" .button-group").replaceWith(environement.comments[comment_id].getAppropiateButtons());
        }
     	
        // mise a jour de l'interface utlisateur apre avoir accepter une demande d'ajout
     	function local_acceptFriend_update(friend_id)
     	{
     		for(var id in environement.comments) // parcourir la liste des commentaires
     		{
     			var comment = environement.comments[id];
     			if(comment.auteur.id == friend_id) // si l'auter du commentaire est l'utilisater que je vien d'accepter sa demande
     			{
                    comment.auteur.contact = 1; 
                    updateCommentButtons(id);
     			}

     		}	
     	}
     	// mise a jour apre refus d'une demande d'ajout
     	function local_refuseFriend_update(friend_id)
     	{
     		for(var id in environement.comments)
     		{
     			var comment = environement.comments[id];
     			if(comment.auteur.id == friend_id)
     			{
     				comment.auteur.contact = 0;
                    updateCommentButtons(id);
     			}

     		}	
     	}
        // accepter une demande d'ajout
     	function acceptFriend(friend_id)
     	{
            $.ajax({
                type: 'POST',
                url: 'friend/accept',
                data: {'friend_id':friend_id},
                success: function(response) {
                    if(response.message != undefined)
                    {
                        showAnError(response.message);
                    }
                    else
                    {
                        local_acceptFriend_update(friend_id);
                        htmlDeleteNotification(friend_id+"request");
                    }
                },
                error: function() {
                    showAnError("Memories n'arrivent pas a satisfaire votre requette");
                }
            });
     	}
        // refuser une demande d'ajout
     	function refuseFriend(friend_id)
     	{
            $.ajax({
                type: 'POST',
                url: 'friend/refuse',
                data: {'friend_id':friend_id},
                success: function(response) {
                    if(response.message != undefined)
                    {
                        showAnError(response.message);
                    }
                    else
                    {
                        local_refuseFriend_update(friend_id);
                        htmlDeleteNotification(friend_id+"request");
                    }
                },
                error: function() {
                    showAnError("Memories n'arrivent pas a satisfaire votre requette");
                }
            });
     	}
     	

        // mise a jour de m'interface apre avoir envoyer une demande d'ajout
		function local_addFriend_update(friend_id)
		{
     		for(var id in environement.comments)
     		{
     			var comment = environement.comments[id];
     			if(comment.auteur.id == friend_id)
     			{
     				comment.auteur.contact = 2;
                    updateCommentButtons(id);
     			}

     		}	
		}
		// mise a jour de l'interface apre suppression d'un(e) ami(e)
		function local_removeFriend_update(friend_id)
		{
     		for(var id in environement.comments)
     		{
     			var comment = environement.comments[id];
     			if(comment.auteur.id == friend_id)
     			{
     				// update the model
     				comment.auteur.contact = 0;
     				// update the view
                   updateCommentButtons(id);
     			}

     		}
		}

		
     	function addFriend(friend_id)
    	{
            // mise a jour de l'interface
     		local_addFriend_update(friend_id);
            // mise a jour de serveur
     		$.ajax({
     			type: 'POST',
     			url: 'friend/add',
     			data: {'friend_id':friend_id},
     			success: function(response) {
     				if(response.message != undefined) // une erreur existe
     				{
                        // mise a jour de l'interface suite a l'erreur
                        local_removeFriend_update(friend_id);
                        // afficher l'erreur
                        showAnError(response.message);     					
     				}
     			},
     			error: function() {
     				local_removeFriend_update(friend_id);
     				showAnError("Memories n'arrivent pas a satisfaire votre requette");
     			}
     		});
     	}

     	function removeFriend(friend_id)
     	{
     		local_removeFriend_update(friend_id);
     		$.ajax({
     			type: 'POST',
     			url: 'friend/remove',
     			data: {'friend_id':friend_id},
     			success: function(response) {
                    if(response.message != undefined)
     				{
     					local_acceptFriend_update(friend_id);
     					showAnError(response.message);
     				}
     			},
     			error: function() {
     				local_acceptFriend_update(friend_id);
     				showAnError("Memories n'arrivent pas a satisfaire votre requette");
     			}
     		});
     	}
     	
     	function local_like_update(comment_id)
     	{
     		// update the model
     		var comment = environement.comments[comment_id];
     		comment.nblike++;
     		comment.ilike = true;
     		// update the view
            updateCommentButtons(comment_id);
     	}
     	
     	function local_unlike_update(comment_id)
     	{
     		// update the model
     		var comment = environement.comments[comment_id];
     		comment.nblike--;
     		comment.ilike = false;
     		// update the view
            updateCommentButtons(comment_id);
     	}

     	function like(comment_id)
     	{
     		local_like_update(comment_id);
     		// server update
     		$.ajax({
     			type:'POST',
     			url:'comment/like',
     			data: {'comment_id':comment_id},
     			success:function(response) {
     				if(response.message != undefined)
     				{
     					local_unlike_update(comment_id);
     					showAnError(response.message);
     				}
     			},
     			error: function() {
     				showAnError("Memories n'arrivent pas a satisfaire votre requette");
     				local_unlike_update(comment_id);
     			}
     		})
     	}

     	function unlike(comment_id)
     	{
     		local_unlike_update(comment_id);
     		// server update
     		$.ajax({
     			type:'POST',
     			url:'comment/unlike',
     			data: {'comment_id':comment_id},
     			success:function(response) {
     				if(response.message != undefined)
     				{
     					local_like_update(comment_id);
     					showAnError(response.message);
     				}
     			},
     			error: function() {
     				showAnError("Memories n'arrivent pas a satisfaire votre requette");
     				local_like_update(comment_id);
     			}
     		})// sync whit the server
     	}
        function consumeNotification(from,type)
        {
            $.ajax({
                type:'POST',
                url:'notification/consume',
                data: {'from':from,'type':type},
                success: function(response) {
                    if(response.message != undefined)
                    {
                        console.log(response.message);
                    }
                },
                error: function() {
                    console.log("consume notfication ajax erreur");
                }
            })
        }
        function notifExists(id)
        {
            if($("#Notif_"+id).length)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
     	function getNotifications()
     	{
            // get notifications
            $.ajax({
                type: 'POST',
                url: 'notifications',
                async:false,
                data: {},
                success: function(response) {
                    if(response.length >= 1)
                    {
                        if(response[0].message != undefined)
                        {
                            showAnError(response[0].message);
                        }
                        else 
                        {
                            for(var id in response)
                            {
                                var e = response[id];
                                
                                if(e.type == "request")
                                {
                                	if(notifExists(e.from+"accept"))
                                	{
                                		htmlDeleteNotification(e.from+"accept");
                                	}
                                    if(!notifExists(e.from+e.type))
                                    {
                                        var notif = new Notification(e.from,e.login,e.type);
                                        htmlAddNotification(notif);
                                    }
                                }
                                else if(e.type == "accept")
                                {
                                    if (!notifExists(e.from+e.type)) 
                                    {
                                        var notif = new Notification(e.from,e.login,e.type);
                                        htmlAddNotification(notif);
                                    }
                                    consumeNotification(e.from,e.type);
                                }
                                else
                                {   
                                	if(notifExists(e.from+"accept"))
                                	{
                                		htmlDeleteNotification(e.from+"accept");
                                	}
                                	consumeNotification(e.from,e.type);
                                }
                                	
                                
                                for(var cid in environement.comments)
                                {
                                    var comment = environement.comments[cid];
                                    if(comment.auteur.id == e.from)
                                    {
                                        if(e.type == "refuse")
                                        {
                                            comment.auteur.contact = 0;
                                            updateCommentButtons(cid);
                                            
                                        }  
                                        else if(e.type == "accept")
                                        {
                                            comment.auteur.contact = 1;
                                            updateCommentButtons(cid);
                                            
                                        }
                                        else if(e.type == "request")
                                        {
                                            comment.auteur.contact = 3;
                                            updateCommentButtons(cid);
                                        }
                                        else if(e.type == "remove")
                                        {
                                            comment.auteur.contact = 0;
                                            updateCommentButtons(cid);
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                error: function() {
                   console.log("erreur ajax on notifications");
                }
            })     		
     	}
     	
     	function getComments()
     	{
            // get comments
            $.ajax({
                type: 'POST',
                url: 'comments',
                async:false,
                data: {},
                success: function(response) {
                    if(response.length >= 1)
                    {
                        if(response[0].message != undefined)
                        {
                           showAnError(response[0].message);    
                        }
                        else
                        {
                            for(var id in environement.comments)
                            {
                                var contain = false;
                                for(var id2 in response)
                                {
                                    if(response[id2].id == id)
                                    {
                                        contain = true;
                                        var comment = environement.comments[id];
                                        comment.nblike = response[id2].nblike;
                                        $("#Comment_"+id+" .button-group").replaceWith(comment.getAppropiateButtons());
                                        break;
                                    }
                                }

                                if(contain == false)
                                {
                                    delete environement.comments[id];
                                    htmlDeleteComment(id);
                                }
                                contain = false;
                            }

                            for(var id in response)
                            {
                                var e = response[id];
                                if(environement.comments[e.id] == undefined)
                                {
                                    var author = new User(e.author.id);
                                    author.login = e.author.login;
                                    author.contact = e.author.contact;
                                    var comment = new Commentaire(e.id);
                                    comment.auteur = author; 
                                    comment.texte = e.texte;
                                    comment.date = new Date(parseInt(e.date));
                                    comment.nblike = e.nblike;
                                    comment.ilike = e.ilike;
                                    htmlAddComment(comment);
                                }
                            }
                        }
                    }
                    else
                    {
                    	for(var id in environement.comments)
                    	{
                    		htmlDeleteComment(id);	
                    	}
                    	environement.comments = [];
                    	environement.users = [];
                    }
                },
                error: function() {
                    console.log("erreur ajax getComments");
                }
                
            });     		
     	}
        getComments();
        getNotifications();
        function getUpdate() {
            getComments();
            getNotifications();
        }
        var update = setInterval(getUpdate, 2500);

        $("#searchArea").on("keypress",function() {
            clearInterval(update);
            var ok = true;
            var onlyFriends = false;

            for(var id in environement.comments)
            {
                var comment = environement.comments[id];
                if(onlyFriends)
                {
                    if(comment.auteur.contact == 0)
                    {
                        ok = false;
                    }
                }

                if(ok)
                {
                    var valeur = $("#searchArea").val();
                    if(comment.texte.search(valeur) == -1)
                    {
                        $("#Comment_"+comment.id).fadeOut('slow');
                    }
                    else
                    {
                        $("#Comment_"+comment.id).fadeIn('slow');   
                    }

                }
                else
                {
                    $("#Comment_"+comment.id).fadeOut('slow');
                }


                ok = true;
            }
        });
    </script>
</body>
