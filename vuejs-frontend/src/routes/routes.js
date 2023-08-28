import DashboardLayout from "@/pages/Layout/DashboardLayout.vue";

import UserProfile from "@/pages/UserProfile.vue";
import CreateAd from "@/pages/CreateAd.vue";
import Page from "@/pages/Page.vue";
import AdDetails from "@/pages/AdDetails.vue";
import Login from "@/pages/Login.vue";

const routes = [
  {
    path: "/",
    component: DashboardLayout,
    redirect: "/page/1",
    children: [
      {
        path: "user",
        name: "User Profile",
        meta: {
          title: "Profil korisnika"
        },
        component: UserProfile
      },
      {
        path: "ad",
        name: "Create Ad",
        meta: {
          title: "Kreiranje oglasa"
        },
        component: CreateAd,
        props: {
          mode: 'create'
        }
      },
      {
        path: "login",
        name: "Login",
        meta: {
          title: "Login"
        },
        component: Login
      },
      {
        path: "ad/:id/edit",
        name: "Edit Ad",
        meta: {
          title: "Izmena oglasa"
        },
        component: CreateAd,
        props: route => ({
          mode: 'edit',
          id: route.params.id
        })
      },
      {
        path: "page/:id",
        name: "Ads",
        meta: {
          title: "Oglasi"
        },
        component: Page
      },
      {
        path: "ad/:id",
        name: "Ad Details",
        meta: {
          title: "Oglas"
        },
        component: AdDetails
      }
    ]
  }
];

export default routes;
