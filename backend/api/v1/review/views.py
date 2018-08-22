from django.conf import settings

from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response

from . import serializers


@api_view(['POST'])
def answer_review_questions(request):
    """View To Submit Ques-Ans For Mess Review"""

    access_token = request.META.get('access_token')
    if access_token == settings.MESS_API_ACCESS_TOKEN:
        data = request.data.get('questions')
        serializer = serializers.ReviewQuestionSerializer(data=data, many=True)

        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    else:
        return Response({'detail': 'NOT AUTHORISED'}, status=status.HTTP_401_UNAUTHORIZED)
