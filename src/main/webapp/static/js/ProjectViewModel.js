function ProjectViewModel() {
	var self = this;

	self.prjName = ko.observable("");

	self.projects = ko.observableArray([]);

	self.query = function() {
		$.ajax({
			method : "POST",
			url : "/project/query",
			data : {
				name : self.prjName
			}
		}).done(function(data) {
			self.projects(data);
		});
	}
}

$( document ).ready(function() {
	ko.applyBindings(new ProjectViewModel());
});