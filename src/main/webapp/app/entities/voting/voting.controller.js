(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingController', VotingController);

    VotingController.$inject = ['Voting'];

    function VotingController(Voting) {

        var vm = this;

        vm.votings = [];

        loadAll();

        function loadAll() {
            Voting.query(function(result) {
                vm.votings = result;
                vm.searchQuery = null;
            });
        }
    }
})();
