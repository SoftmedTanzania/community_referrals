function ANMNavigationPanel(anmNavigationBridge) {
    var populateDataInto = function (cssIdentifierOfRootElement) {
        $(cssIdentifierOfRootElement).html(Handlebars.templates.sidepanel(anmNavigationBridge.getANMInformation()));
    };

    var bindToWorkplan = function (callbackToRunBeforeAnyAction, identifierOfElement) {
        runWithCallBack(callbackToRunBeforeAnyAction, identifierOfElement, function() {
            anmNavigationBridge.delegateToWorkplan();
        });
    };

    var bindToEligibleCoupleList = function (callbackToRunBeforeAnyAction, identifierOfElement) {
        runWithCallBack(callbackToRunBeforeAnyAction, identifierOfElement, function () {
            anmNavigationBridge.delegateToECList();
        });
    };

    var bindToPage = function (callbackToRunBeforeAnyAction, identifierOfElement, pageToGoTo) {
        runWithCallBack(callbackToRunBeforeAnyAction, identifierOfElement, function () {
            window.location.href = pageToGoTo;
        });
    };

    var runWithCallBack = function (callbackToRunBeforeAnyAction, identifierOfElement, action) {
        $(identifierOfElement).click(function () {
            callbackToRunBeforeAnyAction();
            action();
        });
    };

    return {
        populateInto: function(cssIdentifierOfSidePanelElement, callbackToRunBeforeAnyAction) {
            populateDataInto(cssIdentifierOfSidePanelElement);

            bindToWorkplan(callbackToRunBeforeAnyAction, "#workplanButton");
            bindToPage(callbackToRunBeforeAnyAction, "#myStatsButton", "my-stats.html");
            bindToPage(callbackToRunBeforeAnyAction, "#inboxButton", "inbox.html");
            bindToPage(callbackToRunBeforeAnyAction, "#reportsButton", "reports.html");

            bindToEligibleCoupleList(callbackToRunBeforeAnyAction, "#eligibleCoupleMenuOption");
        }
    };
}

function ANMNavigationBridge() {
    var anmNavigationContext = window.navigationContext;
    if (typeof anmNavigationContext === "undefined" && typeof FakeANMNavigationContext !== "undefined") {
        anmNavigationContext = new FakeANMNavigationContext();
    }

    return {
        getANMInformation: function () {
            return JSON.parse(anmNavigationContext.get());
        },
        delegateToECList: function () {
            return anmNavigationContext.startECList();
        },
        delegateToWorkplan: function () {
            return anmNavigationContext.startWorkplan();
        }
    };
}

function FakeANMNavigationContext() {
    return {
        get: function () {
            return "{\"anmName\": \"ANM X\", \"pncCount\": \"4\", \"ancCount\": \"5\", \"childCount\": \"6\", \"eligibleCoupleCount\": \"7\"}";
        },
        startECList: function () {
            window.location.href = "ec_list.html";
        },
        startWorkplan: function () {
            window.location.href = "workplan.html";
        }
    }
}