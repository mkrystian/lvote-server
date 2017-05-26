(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingContentController', VotingContentController);

    VotingContentController.$inject = ['VotingContent'];

    function VotingContentController(VotingContent) {

        var vm = this;

        vm.votingContents = [];

        loadAll();

        function loadAll() {
            VotingContent.query(function(result) {
                vm.votingContents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
