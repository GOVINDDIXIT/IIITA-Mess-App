from django.urls import path

from . import views

urlpatterns = [
    path('', views.ReviewCreateView.as_view()),
]
