'use strict';

function generateSimulationLink(simulationName) {
    return '#' + simulationsRoute + '/' + simulationName;
}

function generatePageLink(simulationName, pageUrl) {
    return generateSimulationLink(simulationName) + '/' + encodeURIComponent(encodeURIComponent(pageUrl));
}

function SimulationsContoller($scope, $http) {
    $scope.refresh = function () {
        $http.get('/simulations').success(function (simulations) {
            $scope.simulations = simulations;
        });
    };
    $scope.refresh();

    $scope.viewSimulationLink = function viewSimulationLink(simulation) {
        return '#' + simulationsRoute + "/" + simulation.name;
    };

    $scope.createSimulation = function () {
        $scope.newSimulation = {name: '', entryUrl: '', urlPrefix: ''};
    };

    $scope.cancelSimulationCreation = function () {
        delete $scope.newSimulation;
    };

    $scope.addNewSimulation = function () {
        $http.post('/simulations', $scope.newSimulation).success(function () {
            $scope.cancelSimulationCreation();
            $scope.refresh();
        });
    };

    function validateNewSimulation(validation_function) {
        if ($scope.newSimulation) {
            return validation_function($scope.newSimulation);
        }
        return false;
    }

    $scope.isNewSimulationNameValid = function () {
        return validateNewSimulation(function (simulation) {
            return simulation.name.trim().length > 0;
        })
    };
    $scope.isNewSimulationUrlValid = function () {
        return validateNewSimulation(function (simulation) {
            return simulation.entryUrl.trim().length > 0;
        })
    };
    $scope.isNewSimulationPrefixValid = function () {
        return validateNewSimulation(function (simulation) {
            return simulation.urlPrefix.trim().length > 0;
        })
    };
    $scope.isNewSimulationValid = function () {
        if ($scope.newSimulation) {
            return validateNewSimulation(function (simulation) {
                return $scope.isNewSimulationNameValid(simulation)
                    && $scope.isNewSimulationUrlValid(simulation)
                    && $scope.isNewSimulationPrefixValid();
            });
        }
        return false;
    }
}

function SimulationController($routeParams, $scope, $http, $location, $filter) {
    var name = $routeParams.name;
    var simulationUrl = '/simulations/' + encodeURIComponent(name);

    $scope.filter = {
        url: '',
        html: true,
        text: true,
        binary: true,
        redirection: true,
        unreachable: true
    };

    $http.get(simulationUrl).success(function (simulation) {
        $scope.simulation = simulation;
    });

    $http.get(simulationUrl + '/pages').success(function (pages) {
        $scope.pages = pages;
    });

    $scope.getPageTitle = function (page) {
        if (page.type == 'HTML') {
            return page.title;
        }
        return 'Unknown title';
    };

    $scope.delete = function () {
        if (confirm('Do you want to delete this simulation?')) {
            $http.delete(simulationUrl).success(function () {
                $location.path(simulationsRoute);
            });
        }
    };

    $scope.generatePageLink = function (page) {
        return generatePageLink(name, page.url);
    };

    function isPageTypeDisplayed(page) {
        var filter = $scope.filter;
        switch (page.type) {
            case 'HTML':
                return filter.html;
            case 'TEXT':
                return filter.text;
            case 'BINARY':
                return filter.binary;
            case 'REDIRECTION':
                return filter.redirection;
            case 'UNREACHABLE':
                return filter.unreachable;
            default:
                throw 'Unknown page type';
        }
    }

    $scope.filterPages = function filterPages() {
        var pages = $scope.pages || [];
        if ($scope.filter.url.length > 0) {
            pages = $filter('filter')(pages, {url: $scope.filter.url});
        }
        return pages.filter(isPageTypeDisplayed);
    };

    $scope.htmlType = 'HTML';
    $scope.textType = 'TEXT';
    $scope.binaryType = 'BINARY';
    $scope.redirectionType = 'REDIRECTION';
    $scope.unreachableType = 'UNREACHABLE';
}

function PageController($routeParams, $scope, $http) {
    var simulationName = $routeParams.name;
    var pageUrl = $routeParams.url;
    var pageWsUrl = '/simulations/' + simulationName + '/pages/' + encodeURIComponent(encodeURIComponent(pageUrl));
    $http.get(pageWsUrl).success(function (page) {
        $scope.page = page;
    });

    $scope.generatePageLink = function (link) {
        return generatePageLink(simulationName, link);
    };

    $scope.generateSimulationLink = function () {
        return generateSimulationLink(simulationName);
    }
}

var simulationsRoute = '/simulations';
var simulationRoute = simulationsRoute + '/:name';
var pageRoute = simulationRoute + '/:url';

angular.module('ajax-crawl-simulator', ['ngRoute'])
    .config(
    [
        '$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when(simulationsRoute, {templateUrl: 'simulations.html', controller: SimulationsContoller}).
                when(simulationRoute, {templateUrl: 'simulation.html', controller: SimulationController}).
                when(pageRoute, {templateUrl: 'page.html', controller: PageController}).
                otherwise({redirectTo: simulationsRoute});
        }
    ])
    .directive('webpageTypeIcon', function () {
        return {
            restrict: 'E',
            scope: {
                type: '=ngModel'
            },
            templateUrl: 'component/webpage-type-icon.html',
            controller: function ($scope) {
                function getPageTypeLabel(type) {
                    switch (type) {
                        case 'HTML':
                            return 'HTML';
                        case 'BINARY':
                            return 'Binary';
                        case 'REDIRECTION':
                            return 'Redirection';
                        case 'TEXT':
                            return 'Text';
                        case 'UNREACHABLE':
                            return 'Unreachable';
                        default:
                            return 'Unknown';
                    }
                }

                $scope.label = function label() {
                    return getPageTypeLabel($scope.type);
                }
            }
        }
    });