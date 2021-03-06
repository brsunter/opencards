(ns cards.views.navigation
  (:require
   [cards.routes :refer [path-for-page]]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [re-frame.core :as re-frame]
   [reagent.core :as r]))

(defn main-app-drawer
  []
  (let [app-drawer-open (re-frame/subscribe [:app-drawer-open])]
    [ui/drawer {:open @app-drawer-open
                :docked false
                :z-depth 2
                :on-request-change #(re-frame/dispatch [:set-nav-drawer-open %])}
     [ui/menu
      [ui/divider]
      [ui/menu-item {:primary-text "Feed"
                     :href (path-for-page :feed)
                     :left-icon (ic/communication-rss-feed)}]
      [ui/divider]
      [ui/menu-item {:primary-text "Cards"
                     :href (path-for-page :cards)
                     :left-icon (ic/hardware-sim-card)}]
      [ui/divider]
      [ui/menu-item {:primary-text "Decks"
                     :href (path-for-page :decks)
                     :left-icon (ic/hardware-dock)}]
      [ui/divider]]]))

(defn add-deck-button
  []
  (let [enabled (re-frame/subscribe [:add-deck-button-enabled])
        add-deck-title (re-frame/subscribe [:add-deck-title])
        add-deck-description (re-frame/subscribe [:add-deck-description])]
    [ui/flat-button {:label "save"
                     :on-click #(re-frame/dispatch [:add-deck @add-deck-title @add-deck-description])
                     :disabled (not @enabled)
                     :style {:color "white"
                             :margin-top 5}}]))

(defn add-card-button
  []
  (let [enabled (re-frame/subscribe [:add-card-button-enabled])
        add-card-front-text (re-frame/subscribe [:add-card-front-text])
        add-card-back-text (re-frame/subscribe [:add-card-back-text])]
    [ui/flat-button {:label "save"
                     :on-click #(re-frame/dispatch [:add-card/create-card @add-card-front-text @add-card-back-text])
                     :disabled (not @enabled)
                     :style {:color "white"
                             :margin-top 5}}]))

(defn right-app-bar-button-for-page
  [page]
  (case page
    :add-deck [add-deck-button]
    :add-card [add-card-button]
    [ui/icon-button]))

(defn app-bar-close-button
  [props]
  [ui/icon-button props [ic/navigation-close {:style {:fill "white"}}]])

(defn app-bar-menu-button
  []
  [ui/icon-button {:on-click #(re-frame/dispatch [:toggle-nav-drawer])}
   [ic/navigation-menu {:style {:fill "white"}}]])

(defn left-app-bar-button-for-page
  [page]
  (case page
    :add-deck [app-bar-close-button {:href (path-for-page :decks)}]
    :add-card [app-bar-close-button {:href (path-for-page :cards)}]
    [app-bar-menu-button]))

(defn main-app-bar
  [page]
  (let [title (re-frame/subscribe [:active-panel-title])]
    [ui/app-bar {:title @title
                 :z-depth 2
                 :icon-element-left (r/as-element [left-app-bar-button-for-page page])
                 :icon-element-right (r/as-element [right-app-bar-button-for-page page])
                 :style {:position "fixed"
                         :top 0
                         :left 0}}
     [main-app-drawer]]))

(defn navigation [content]
  (let [page (re-frame/subscribe [:active-panel])]
    (fn [content]
      [ui/mui-theme-provider
       {:mui-theme (get-mui-theme
                    {:palette {:text-color (color :green600)}})}
       [:div {:style {:margin-top 100}}
        [main-app-bar @page]
        [content]]])))

(def tab-bar-style
  {:margin 0
   :top "auto"
   :bottom 0
   :left "auto"
   :text-align "center"
   :position "fixed"})

(defn bottom-navigation
  [tab-bar-index]
  [ui/paper
   [ui/bottom-navigation {:selected-index tab-bar-index
                          :style tab-bar-style}
    [ui/bottom-navigation-item {:label "Feed"
                                :icon (ic/av-featured-play-list)
                                :href (path-for-page :feed)}]
    [ui/bottom-navigation-item {:label "Add"
                                :href (path-for-page :add-card)
                                :icon (ic/content-add)}]
    [ui/bottom-navigation-item {:label "Home"
                                :href (path-for-page :home)
                                :icon (ic/action-home)}]]])
