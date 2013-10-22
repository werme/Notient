
// This file is ugly and full of bad practices. Close your eyes.
// We plan to implement some kind of data-binding framework at a later stage.  

;

(function(){

  // This runs at page load
  $('#quick-create-form').css({
    "top": -$(this).height(),
    "margin-bottom": -$(this).height()
  });

  var toggleMenu = function() {
    e = $('#quick-create-form');
    e.css("display", "block");

    if(e.css('top') === '0px') {
      e.css('top', -e.height());
      e.css('margin-bottom', -e.height());
    } else {
      e.css('top', 0);
      e.css('margin-bottom', 0);
    }
  };

  // Binds at page load
  $('#sub-nav-toggle').click(function() {
    toggleMenu();
  });

  // Binds at page load
  $('.quick-delete-note-button').click(function() {
    // TODO: Make dialog modules
    confirm("Are you sure you want to delete this note?");
    $.post($(this).data("url"), function() {
      location.reload();
    });
  });

  // Hide notification after 5 seconds
  $("#message-wrapper").delay(5000).slideUp();
})();
