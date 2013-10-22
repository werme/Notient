;

(function(){

  e = $('#quick-note-form');
  e.css({
    "top": -e.height(),
    "margin-bottom": -e.height()
  });

  var toggleMenu = function() {
    e.css("display", "block");

    if(e.css('top') === '0px') {
      e.css('top', -e.height());
      e.css('margin-bottom', -e.height());
    } else {
      e.css('top', 0);
      e.css('margin-bottom', 0);
    }
  };

  $('#sub-nav-toggle').click(function() {
    toggleMenu();
  });

  $('.quick-delete-note-button').click(function() {
    // TODO: Make dialog and notification modules
    confirm("Are you sure you want to delete this note?");
    $.post($(this).data("url"));
  });

  $('.dismiss-message-button').click(function() {
    $('#message-wrapper').slideUp();
  });

  $("#message-wrapper").delay(10000).slideUp();
})();
