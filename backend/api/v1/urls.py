from django.urls import path

from .review import views as review_views

urlpatterns = [
    path('review/', review_views.answer_review_questions,
         name="answer_review_ques"),
]
