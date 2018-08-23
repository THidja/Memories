$(function() {
    $('.page-alert').hide();
    $('.page-alert').attr("disabled","");
    //Show alert
    $("#alert").on('alertEvent',function(e) {
        e.preventDefault();
        var alert = $('#alert');
        var timeOut;
        alert.appendTo('.page-alerts');
        alert.slideDown();
        
        //Is autoclosing alert
        var delay = $(this).attr('data-delay');
        if(delay != undefined)
        {
            delay = parseInt(delay);
            clearTimeout(timeOut);
            timeOut = window.setTimeout(function() {
                    alert.slideUp();
                }, delay);
        }
    });
    
    //Close alert
    $('.page-alert .close').click(function(e) {
        e.preventDefault();
        $(this).closest('.page-alert').slideUp();
    });
    
});