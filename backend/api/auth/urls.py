from django.urls import path

from . import views

urlpatterns = [
    path('register/', views.UserProfileAPIView.as_view()),
]
