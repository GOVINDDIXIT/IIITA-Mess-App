from rest_framework import serializers

from review.models import Review


class ReviewSerializer(serializers.ModelSerializer):
    """Serializer For Answering Questions For Reviews"""

    class Meta:
        model = Review
        fields = ['user', 'ques', 'review', 'rating', 'review']
