from django.conf import settings
from django.utils import timezone

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from . import serializers
from review.models import ReviewQuestion


@api_view(['POST'])
def answer_review_questions(request):
    """View To Submit Ques-Ans For Mess Review"""

    access_token = request.META.get('HTTP_TOKEN')
    if access_token == settings.MESS_API_ACCESS_TOKEN:
        if (ReviewQuestion.objects.all().count() > 0):
            last_post_date = ReviewQuestion.objects.all().order_by(
                '-timestamp').first().timestamp.date()
            today_date = timezone.now().date()
            if (last_post_date == today_date):
                return Response({'detail': 'Data Already Submitted'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        data = request.data.get('questions')
        serializer = serializers.ReviewQuestionSerializer(data=data, many=True)

        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    else:
        return Response({'detail': 'NOT AUTHORISED'}, status=status.HTTP_401_UNAUTHORIZED)
