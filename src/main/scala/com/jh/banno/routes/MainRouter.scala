package com.jh.banno.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.google.inject.Inject

class MainRouter @Inject()(apiRouter: ApiRouter) {
    val apiRoutes: Route = apiRouter.routes

    def routes: Route = {
        pathPrefix("api") { apiRoutes }
    }

}
