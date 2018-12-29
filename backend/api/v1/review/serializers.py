from rest_framework import serializers

from review_ques.models import ReviewQuestion


class ReviewQuestionSerializer(serializers.ModelSerializer):
    """Serializer To Answer Review Questions"""

    class Meta:
        model = ReviewQuestion
        fields = ['ques', 'check', 'comment', 'ques_type']
