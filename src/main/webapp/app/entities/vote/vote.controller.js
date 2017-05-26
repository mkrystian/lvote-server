(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VoteController', VoteController);

    VoteController.$inject = ['Vote'];

    function VoteController(Vote) {

        var vm = this;

        vm.votes = [];

        loadAll();

        function loadAll() {
            Vote.query(function(result) {
                vm.votes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
