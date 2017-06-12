(function () {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingAnswerController', VotingAnswerController);

    VotingAnswerController.$inject = ['VotingAnswer'];

    function VotingAnswerController(VotingAnswer) {

        var vm = this;

        vm.votingAnswers = [];

        loadAll();

        function loadAll() {
            VotingAnswer.query(function (result) {
                vm.votingAnswers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
